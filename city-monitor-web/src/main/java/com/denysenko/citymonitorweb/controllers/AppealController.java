package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.services.converters.impl.AppealEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/appeals")
public class AppealController {
    @Autowired
    private AppealService appealService;
    @Autowired
    private AppealEntityToDTOConverter appealConverter;
    @Value("${citymonitor.googlemaps.apikey}")
    private String GOOGLE_MAPS_API_KEY;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals() {
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('appeals:read', 'appeals:write')")
    public String appealsPage(Model model,
                              @RequestParam(name = "tab", defaultValue = "all") String tabName,
                              @RequestParam(name = "page", defaultValue = "1", required = false) int pageNumber,
                              @RequestParam(name = "size", defaultValue = "15", required = false) int pageSize) {

        Set<AppealStatus> statusSet;
        if (tabName.equalsIgnoreCase("ALL")) {
            statusSet = Set.of(AppealStatus.UNREAD, AppealStatus.IN_PROGRESS, AppealStatus.VIEWED, AppealStatus.PROCESSED);
        } else if (tabName.equalsIgnoreCase("NEW")) {
            statusSet = Set.of(AppealStatus.UNREAD);
        } else if (tabName.equalsIgnoreCase("IN_PROGRESS")) {
            statusSet = Set.of(AppealStatus.IN_PROGRESS);
        } else if (tabName.equalsIgnoreCase("PROCESSED")) {
            statusSet = Set.of(AppealStatus.PROCESSED);
        } else if (tabName.equalsIgnoreCase("TRASH")) {
            statusSet = Set.of(AppealStatus.TRASH);
        } else throw new InputValidationException("Таби з назвою '" + tabName + "' не існує");

        if (pageNumber < 1 || pageSize < 1)
            throw new InputValidationException("Номер сторінки та її розмір має бути більше нуля. Поточні значення: pageNumber = " + pageNumber + ", pageSize = " + pageSize);

        Page<Appeal> appeals = appealService.getPageByStatuses(pageNumber, pageSize, statusSet);
        Page<AppealDTO> appealDTOs = appeals.map(appeal -> appealConverter.convertEntityToDTO(appeal));
        model.addAttribute("tab", tabName);
        model.addAttribute("appeals", appealDTOs);
        model.addAttribute("page", pageNumber);
        model.addAttribute("size", pageSize);
        model.addAttribute("googlemaps_apikey", GOOGLE_MAPS_API_KEY);

        return "appeals/appeals";
    }

    @PatchMapping("/{id}/changeStatus")
    @PreAuthorize("hasAuthority('appeals:write')")
    public ResponseEntity changeStatus(@PathVariable(name = "id") long id, @RequestParam(name = "status") String status) {
        try {
            AppealStatus appealStatus = AppealStatus.valueOf(status);
            appealService.updateStatusById(id, appealStatus);
        } catch (IllegalArgumentException e){
            throw new RestException("Неприйнятний статус: " + status, e, HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body("{\"msg\":\"success\"}");
    }

    @PatchMapping("/changeStatuses")
    @PreAuthorize("hasAuthority('appeals:write')")
    public ResponseEntity changeStatus(@RequestParam(name = "ids") Set<String> ids, @RequestParam(name = "status") String status) {
        try {
            AppealStatus appealStatus = AppealStatus.valueOf(status);
            ids.forEach(id -> appealService.updateStatusById(Long.valueOf(id), appealStatus));
        } catch (IllegalArgumentException e){
            throw new RestException("Неприйнятний статус: " + status, e, HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body("{\"msg\":\"success\"}");
    }
}
