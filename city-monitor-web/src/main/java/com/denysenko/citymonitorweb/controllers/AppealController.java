package com.denysenko.citymonitorweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/appeals")
public class AppealController {

    @GetMapping()
    public String appealsPage(Model model,
                              @RequestParam(name = "tab", defaultValue = "all") String tabName,
                              @RequestParam(defaultValue = "1", required = false) int page,
                              @RequestParam(defaultValue = "30", required = false) int size){
        model.addAttribute("tab", tabName);
        model.addAttribute("appeals", null);
        model.addAttribute("unreadAppealsCnt", 20);
        return "appeals/appeals";
    }
}
