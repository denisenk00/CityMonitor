package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.services.entity.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/appeals")
public class AppealController {
    @Autowired
    private AppealService appealService;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    public String appealsPage(Model model,
                              @RequestParam(name = "tab", defaultValue = "all") String tabName,
                              @RequestParam(defaultValue = "1", required = false) int page,
                              @RequestParam(defaultValue = "30", required = false) int size){
        model.addAttribute("tab", tabName);
        model.addAttribute("appeals", null);
        return "appeals/appeals";
    }
}
