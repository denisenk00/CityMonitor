package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.services.entity.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('users:rw')")
public class UserController {
    @Autowired
    private AppealService appealService;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    public String usersPage(){
        return "";
    }

    @GetMapping("/{id}")
    public String userProfilePage(@PathVariable("id") int id){
        return "";
    }

    @GetMapping("/new")
    public String newUserProfile(){
        return "";
    }

    @PostMapping()
    public String saveUserProfile(){
        return "";
    }

    @PatchMapping("/{id}/update")
    public String updateUserProfile(@PathVariable("id") int id){
        return "";
    }


}
