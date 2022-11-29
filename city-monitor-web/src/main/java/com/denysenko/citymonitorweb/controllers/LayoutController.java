package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.exceptions.RestException;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.converters.impl.LayoutEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/layouts")
public class LayoutController {
    @Value("${citymonitor.maps.center.lat}")
    private String mapCenterLat;
    @Value("${citymonitor.maps.center.lng}")
    private String mapCenterLng;
    @Value("${citymonitor.maps.zoom}")
    private String mapZoom;
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private LayoutEntityToDTOConverter layoutConverter;
    @Autowired
    private AppealService appealService;

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
        return "layouts/newLayout";
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('layouts:write')")
    public String saveLayout(@Valid @ModelAttribute("layout") LayoutDTO layoutDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new IllegalArgumentException();

        Layout layout = layoutConverter.convertDTOToEntity(layoutDTO);
        layoutService.saveLayout(layout);
        return "redirect:/";
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('layouts:read')")
    public String layouts(Model model, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "30", required = false) int size) {
        Page<Layout> layoutPage = layoutService.getPageOfLayouts(page, size);
        Page<LayoutDTO> dtoPage = layoutPage.map(layout -> layoutConverter.convertEntityToDTO(layout));
        Paged<LayoutDTO> paged = new Paged(dtoPage, Paging.of(dtoPage.getTotalPages(), page, size));

        model.addAttribute("layouts", paged);
        return "layouts/layouts";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('layouts:read')")
    public String layoutPage(@PathVariable(name = "id") long id, Model model){
        Layout layout = layoutService.getLayoutById(id);
        LayoutDTO layoutDTO = layoutConverter.convertEntityToDTO(layout);
        List<Quiz> quizList = quizService.findQuizzesByLayoutId(layout.getId());
        model.addAttribute("mapCenterLat", mapCenterLat);
        model.addAttribute("mapCenterLng", mapCenterLng);
        model.addAttribute("mapZoom", mapZoom);
        model.addAttribute("layout", layoutDTO);
        model.addAttribute("quizzes", quizList);
        return "layouts/layout";
    }

    @PatchMapping("/{id}/changeAvailability")
    @PreAuthorize("hasAuthority('layouts:write')")
    public ResponseEntity changeLayoutAvailability(@PathVariable(name = "id") long id, @RequestParam(name = "deprecated") boolean isDeprecated){
        LayoutStatus newlayoutStatus;
        try {
            if(isDeprecated){
                newlayoutStatus = layoutService.markLayoutAsDeprecated(id);
            }
            else{
                newlayoutStatus = layoutService.markLayoutAsActual(id);
            }
        }catch (IllegalArgumentException e){
            throw new RestException("", e);
        }
        String body = "{\"msg\":\"success\", \"newStatus\":\"" + newlayoutStatus.getTitle() + "\"}";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('layouts:write')")
    public ResponseEntity deleteLayout(@PathVariable(name = "id") long id){
        layoutService.deleteLayout(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }
}
