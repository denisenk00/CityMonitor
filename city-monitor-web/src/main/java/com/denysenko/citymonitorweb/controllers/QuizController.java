package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.LayoutService;
import com.denysenko.citymonitorweb.services.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private LayoutService layoutService;

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
    public String newQuiz(@ModelAttribute(name = "quiz") QuizDTO quiz, Model model){
        quiz.setOptionDTOs(List.of(new OptionDTO(), new OptionDTO()));
        model.addAttribute("layouts", layoutService.getAllLayouts());
        return "quizzes/newQuiz";
    }

    @GetMapping("/new/addOption")
    public String addOption(@ModelAttribute(name = "quiz") Quiz quiz,
                            @ModelAttribute(name = "files") List<MultipartFile> files,
                            @RequestParam(name = "files", required = false) List<MultipartFile> f){
        System.out.println("Add option");
        quiz.getOptions().add(new Option());
        return "quizzes/newQuiz";
    }

    @GetMapping("/new/removeOption")
    public String removeOption(@RequestParam("oindex") int oIndex,
                               @ModelAttribute(name = "files") List<MultipartFile> files){
        return "";
    }

    @PostMapping()
    public void addQuiz(@ModelAttribute(name = "quiz") QuizDTO quizDTO){

    }

    @DeleteMapping("/{id}")
    public void removeQuiz(@PathVariable Long id){

    }

    @PatchMapping("/{id}/finish")
    public void finishQuiz(@PathVariable Long id){

    }
}
