package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.converters.impl.LayoutEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/layouts")
public class LayoutController {
    @Value("${citymonitor.maps.center.lat}")
    private String mapCenterLat;
    @Value("${citymonitor.maps.center.lng}")
    private String mapCenterLng;
    @Value("${citymonitor.maps.zoom}")
    private String mapZoom;
    @Value("${citymonitor.googlemaps.apikey}")
    private String GOOGLE_MAPS_API_KEY;

    private final LayoutService layoutService;
    private final QuizService quizService;
    private final LayoutEntityToDTOConverter layoutConverter;
    private final AppealService appealService;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }


    @GetMapping("/new")
    @PreAuthorize("hasAuthority('layouts:write')")
    public String newLayout(@ModelAttribute("layout") LayoutDTO layoutDTO,  Model model){
        model.addAttribute("mapCenterLat", mapCenterLat);
        model.addAttribute("mapCenterLng", mapCenterLng);
        model.addAttribute("mapZoom", mapZoom);
        model.addAttribute("googlemaps_apikey", GOOGLE_MAPS_API_KEY);
        return "layouts/newLayout";
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('layouts:write')")
    public String saveLayout(@Valid @ModelAttribute("layout") LayoutDTO layoutDTO){
        Layout layout = layoutConverter.convertDTOToEntity(layoutDTO);
        layoutService.saveLayout(layout);
        return "redirect:/";
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('layouts:read')")
    public String layouts(Model model,
                          @RequestParam(name = "page", defaultValue = "1", required = false) int pageNumber,
                          @RequestParam(name = "size", defaultValue = "30", required = false) int pageSize) {
        if (pageNumber < 1 || pageSize < 1)
            throw new InputValidationException("Номер сторінки та її розмір має бути більше нуля. Поточні значення: pageNumber = " + pageNumber + ", pageSize = " + pageSize);

        Page<Layout> layoutPage = layoutService.getPageOfLayouts(pageNumber, pageSize);
        Page<LayoutDTO> dtoPage = layoutPage.map(layout -> layoutConverter.convertEntityToDTO(layout));
        Paged<LayoutDTO> paged = new Paged(dtoPage, Paging.of(dtoPage.getTotalPages(), pageNumber, pageSize));

        model.addAttribute("layouts", paged);
        return "layouts/layouts";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('layouts:read')")
    public String layoutPage(@PathVariable(name = "id") long id, Model model){
        Layout layout;
        try {
             layout = layoutService.getLayoutById(id);
        }catch (EntityNotFoundException e){
            throw new InputValidationException(e.getMessage(), e);
        }

        LayoutDTO layoutDTO = layoutConverter.convertEntityToDTO(layout);
        List<Quiz> quizList = quizService.findQuizzesByLayoutId(layout.getId());
        model.addAttribute("mapCenterLat", mapCenterLat);
        model.addAttribute("mapCenterLng", mapCenterLng);
        model.addAttribute("mapZoom", mapZoom);
        model.addAttribute("googlemaps_apikey", GOOGLE_MAPS_API_KEY);
        model.addAttribute("layout", layoutDTO);
        model.addAttribute("quizzes", quizList);
        return "layouts/layout";
    }

    @PatchMapping("/{id}/changeAvailability")
    @PreAuthorize("hasAuthority('layouts:write')")
    public ResponseEntity changeLayoutAvailability(@PathVariable(name = "id") long id, @RequestParam(name = "deprecated") boolean isDeprecated){
        LayoutStatus newStatus;
        try {
            if(isDeprecated){
                newStatus = layoutService.markLayoutAsDeprecated(id);
            }
            else{
                newStatus = layoutService.markLayoutAsActual(id);
            }
        }catch (EntityNotFoundException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String body = "{\"msg\":\"success\", \"newStatus\":\"" + newStatus.getTitle() + "\"}";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('layouts:write')")
    public ResponseEntity deleteLayout(@PathVariable(name = "id") long id){
        try {
            layoutService.deleteLayout(id);
        }catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }
}
