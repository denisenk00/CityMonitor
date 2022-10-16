package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/layouts")
public class LayoutController {
    @Value("${citymonitor.maps.center.lat}")
    private String mapCenterLat;
    @Value("${citymonitor.maps.center.lng}")
    private String mapCenterLng;
    @Value("${citymonitor.maps.zoom}")
    private String mapZoom;


    @GetMapping("/new")
    public String newLayout(@ModelAttribute("layout") LayoutDTO layoutDTO,  Model model){
        model.addAttribute("mapCenterLat", mapCenterLat);
        model.addAttribute("mapCenterLng", mapCenterLng);
        model.addAttribute("mapZoom", mapZoom);
        return "layouts/newLayout";
    }

    @PostMapping("/")
    public String saveLayout(@ModelAttribute("layout") LayoutDTO layoutDTO){
        System.out.println(layoutDTO.toString());
        return "redirect:/";
    }

    @GetMapping()
    public String layouts() { return "layouts/layouts";}
}
