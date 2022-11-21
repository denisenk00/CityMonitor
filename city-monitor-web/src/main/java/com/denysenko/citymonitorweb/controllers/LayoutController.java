package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.services.converters.impl.LayoutEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


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
    private LayoutEntityToDTOConverter entityDTOConverter;
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

        Layout layout = entityDTOConverter.convertDTOToEntity(layoutDTO);
        layoutService.saveLayout(layout);
        return "redirect:/";
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('layouts:read')")
    public String layouts() { return "layouts/layouts";}
}
