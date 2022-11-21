package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizEntityToDTOConverter converter;
    @Autowired
    private AppealService appealService;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping("/")
    public String index(Model model){
        List<Quiz> lastQuizzes = quizService.getLast10Quizzes();
        List<QuizDTO> quizDTOs = converter.convertListsEntityToDTO(lastQuizzes);
        model.addAttribute("quizzes", quizDTOs);
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
