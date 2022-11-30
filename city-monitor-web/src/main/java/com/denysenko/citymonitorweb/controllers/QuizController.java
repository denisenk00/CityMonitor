package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.QuizFinisher;
import com.denysenko.citymonitorweb.services.QuizSender;
import com.denysenko.citymonitorweb.services.converters.impl.MultiFileToDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private QuizSender quizSender;
    @Autowired
    private QuizFinisher quizFinisher;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizzesPage(Model model, @RequestParam(defaultValue = "1", required = false) int page, @RequestParam(defaultValue = "30", required = false) int size){

        Page<Quiz> quizzesPage = quizService.getPageOfQuizzes(page, size);
        Page<QuizDTO> dtoPage = quizzesPage.map(quiz -> quizConverter.convertEntityToDTO(quiz));
        Paged<QuizDTO> paged = new Paged(dtoPage, Paging.of(dtoPage.getTotalPages(), page, size));

        model.addAttribute("quizzes", paged);
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
        model.addAttribute("layouts", layoutService.getNotDeprecatedLayouts());
        return "quizzes/newQuiz";
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public String saveQuiz(@Valid @ModelAttribute("quiz") QuizDTO quizDTO, BindingResult bindingResult,
                           @ModelAttribute("files") List<MultipartFile> files,
                           @ModelAttribute(name = "selectedLayoutId") String layoutId){
        if((!quizDTO.isStartImmediate() && Objects.isNull(quizDTO.getStartDate()))
                || bindingResult.hasErrors() || !isQuizPeriodCorrect(quizDTO) || layoutId == null) {
            throw new IllegalArgumentException();
        }

        List<FileDTO> fileDTOs = mFileConverter.convertListOfMultipartFileToDTO(files);
        quizDTO.setFileDTOs(fileDTOs);
        Layout layout = layoutService.getLayoutById(Long.valueOf(layoutId));

        if(layout.getStatus().equals(LayoutStatus.DEPRECATED)) throw new IllegalArgumentException();

        layout.setStatus(LayoutStatus.IN_USE);
        Quiz quiz = quizConverter.convertDTOToEntity(quizDTO);
        quiz.setLayout(layout);
        quizService.saveQuiz(quiz);

        if(quizDTO.isStartImmediate()){
            quizSender.sendImmediate(quiz);
        }else {
            quizSender.schedule(quiz);
        }

        quizFinisher.schedule(quiz);

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public ResponseEntity removeQuiz(@PathVariable Long id){
        //update layout status
        quizSender.removeScheduledQuiz(id);
        quizFinisher.removeScheduledFinish(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public ResponseEntity finishQuiz(@PathVariable(name = "id") Long id) throws InterruptedException {
        Quiz quiz = quizService.getById(id);
        quizFinisher.finishImmediate(quiz);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }

    private boolean isQuizPeriodCorrect(QuizDTO quizDTO){
        boolean startImmediate = quizDTO.isStartImmediate();
        LocalDateTime startDate = quizDTO.getStartDate();
        LocalDateTime endDate = quizDTO.getEndDate();
        return (startImmediate && endDate.isAfter(LocalDateTime.now()))
                || (startDate.isAfter(LocalDateTime.now()) && startDate.isBefore(endDate));
    }

}
