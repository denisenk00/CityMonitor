package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.services.FileService;
import com.denysenko.citymonitorweb.services.LayoutService;
import com.denysenko.citymonitorweb.services.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private FileService fileService;

    @GetMapping()
    public String quizzesPage(Model model, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "30", required = false) int size){
        System.out.println("quizzesPage");
        model.addAttribute("quizzes", quizService.getPageOfQuizzes(page, size));
        return "quizzes/quizzes";
    }

    @GetMapping("/{id}")
    public String quizPage(Model model, @PathVariable("id") int id){
        return "quizzes/quiz";
    }

    @GetMapping("/new")
    public String newQuiz(@ModelAttribute(name = "quiz") QuizDTO quiz, @ModelAttribute(name = "files") List<MultipartFile> files, Model model){
        System.out.println("newQuiz");
        quiz.setStartImmediate(true);
        quiz.setOptionDTOs(List.of(new OptionDTO(), new OptionDTO()));
        model.addAttribute("layouts", layoutService.getAllLayouts());
        model.addAttribute("unreadAppealsCnt", 10);
        return "quizzes/newQuiz";
    }

    @PostMapping("/")
    public String saveQuiz(@Valid @ModelAttribute("quiz") QuizDTO quizDTO, BindingResult bindingResult, @ModelAttribute("files") List<MultipartFile> files, Model model){
        System.out.println("saving + " + quizDTO + ", files = " + files.size());
        files.forEach(a -> System.out.println(a.getOriginalFilename() + ", "));
        if(!quizDTO.isStartImmediate() && Objects.isNull(quizDTO.getStartDate())){
            String message = "Визначте час початку опитування";
            FieldError incorrectStartDate = new FieldError("quiz", "startDate", message);
            bindingResult.addError(incorrectStartDate);
        }
        model.asMap().entrySet().forEach(a -> System.out.println("key " + a.getKey() + ", value = " + a.getValue()));
        if(bindingResult.hasErrors()) {
            model.addAttribute("files", files);
            System.out.println("hasErrors");
            return "quizzes/newQuiz";
        }

        if(!isQuizPeriodCorrect(quizDTO)) throw new IllegalArgumentException();

        List<FileDTO> fileDTOs = fileService.convertListOfMultipartFileToDTO(files);
        quizDTO.setFileDTOs(fileDTOs);
        quizService.saveQuiz(quizDTO);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public void removeQuiz(@PathVariable Long id){

        return;
    }

    @PatchMapping("/{id}/finish")
    public void finishQuiz(@PathVariable Long id){
        System.out.println("3");
    }

    private boolean isQuizPeriodCorrect(QuizDTO quizDTO){
        boolean startImmediate = quizDTO.isStartImmediate();
        LocalDateTime startDate = quizDTO.getStartDate();
        LocalDateTime endDate = quizDTO.getEndDate();
        return (startImmediate && endDate.isAfter(LocalDateTime.now()))
                || (startDate.isAfter(LocalDateTime.now()) && startDate.isBefore(endDate));
    }

}
