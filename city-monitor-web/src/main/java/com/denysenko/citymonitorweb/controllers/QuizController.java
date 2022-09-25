package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping()
    public String quizzesPage(Model model, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "30", required = false) int size){
        model.addAttribute("quizzes", quizService.getPageOfQuizzes(page, size));
        return "quizzes/quizzes";
    }

    @GetMapping("/{id}")
    public String quizPage(Model model, @PathVariable("id") int id){

        return "quizzes/quiz";
    }

    @GetMapping("/new")
    public String newQuiz(){
        return "quizzes/newQuiz";
    }

    @PostMapping()
    public void addQuiz(){

    }

    @DeleteMapping("/{id}")
    public void removeQuiz(@PathVariable Long id){

    }

    @PatchMapping("/{id}/finish")
    public void finishQuiz(@PathVariable Long id){

    }
}
