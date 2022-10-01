package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private QuizService quizService;

    @GetMapping("/")
    public String index(Model model){
        List<Quiz> lastQuizzes = quizService.getFirstNQuizzes(10);
        model.addAttribute("quizzes", lastQuizzes);
        return "index";
    }
}
