package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.converters.impl.LayoutEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.MultiFileToDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private AppealService appealService;
    @Autowired
    private MultiFileToDTOConverter mFileConverter;
    @Autowired
    private QuizEntityToDTOConverter quizConverter;
    @Autowired
    private LayoutEntityToDTOConverter layoutConverter;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizzesPage(Model model, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "30", required = false) int size){
        System.out.println("\n\n\n\n\n\n\n\n\nquizzesPage page = " + page + " , size = " + size);
        model.addAttribute("quizzes", quizService.getPageOfQuizzes(page, size));
        return "quizzes/quizzes";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizPage(Model model, @PathVariable("id") int id){
        Quiz quiz = quizService.getById(Long.valueOf(id));
        QuizDTO quizDTO = quizConverter.convertEntityToDTO(quiz);
        model.addAttribute("quiz", quizDTO);
        //need to add results if exist
        return "quizzes/quiz";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public String newQuiz(@ModelAttribute(name = "quiz") QuizDTO quiz, @ModelAttribute(name = "files") List<MultipartFile> files,
                          @ModelAttribute(name = "selectedLayoutId") String layoutId, Model model){
        quiz.setStartImmediate(true);
        quiz.setOptionDTOs(List.of(new OptionDTO(), new OptionDTO()));
        model.addAttribute("layouts", layoutService.getAllLayouts());
        return "quizzes/newQuiz";
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public String saveQuiz(@Valid @ModelAttribute("quiz") QuizDTO quizDTO, BindingResult bindingResult,
                           @ModelAttribute("files") List<MultipartFile> files,
                           @ModelAttribute(name = "selectedLayoutId") String layoutId){
        System.out.println("layout ID = " + layoutId);
        if((!quizDTO.isStartImmediate() && Objects.isNull(quizDTO.getStartDate()))
                || bindingResult.hasErrors() || !isQuizPeriodCorrect(quizDTO) || layoutId == null) {
            throw new IllegalArgumentException();
        }

        List<FileDTO> fileDTOs = mFileConverter.convertListOfMultipartFileToDTO(files);
        quizDTO.setFileDTOs(fileDTOs);
        Layout layout = layoutService.getLayoutById(Long.valueOf(layoutId));

        Quiz quiz = quizConverter.convertDTOToEntity(quizDTO);
        quiz.setLayout(layout);
        quizService.saveQuiz(quiz);

        //NEED TO SEND QUIZ TO LOCALS IN BOT

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public void removeQuiz(@PathVariable Long id){

        return;
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
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
