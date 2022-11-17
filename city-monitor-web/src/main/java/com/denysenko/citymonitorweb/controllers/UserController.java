package com.denysenko.citymonitorweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

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
