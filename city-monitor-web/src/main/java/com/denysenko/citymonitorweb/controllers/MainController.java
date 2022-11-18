package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private QuizService quizService;

    @GetMapping("/")
    public String index(Model model){
        List<QuizDTO> lastQuizzes = quizService.getFirstNQuizzes(10);
        model.addAttribute("quizzes", lastQuizzes);
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) boolean loginError, Model model){
        if(loginError){
            String failedLoginMessage = "Ім'я користувача або пароль не вірні. Спробуйте ще раз";
            model.addAttribute("error", failedLoginMessage);
        }
        return "login";
    }

    @PatchMapping("/myprofile/update")
    public String updateCurrentProfile(@PathVariable("id") int id){
        return "";
    }

    @GetMapping("/myprofile")
    public String profilesPage(){
        return "";
    }
}
