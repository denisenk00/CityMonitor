package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.dto.ResultDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.services.QuizFinisher;
import com.denysenko.citymonitorweb.services.QuizSender;
import com.denysenko.citymonitorweb.services.converters.impl.MultiFileToDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.ResultEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.entity.ResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Log4j
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
    private ResultService resultService;
    @Autowired
    private MultiFileToDTOConverter mFileConverter;
    @Autowired
    private QuizEntityToDTOConverter quizConverter;
    @Autowired
    private ResultEntityToDTOConverter resultConverter;
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
        log.info("Getting quizzes page with parameters: page = " + page + ", size = " + size);
        Page<Quiz> quizzesPage = quizService.getPageOfQuizzes(page, size);
        Page<QuizDTO> dtoPage = quizzesPage.map(quiz -> quizConverter.convertEntityToDTO(quiz));
        Paged<QuizDTO> paged = new Paged(dtoPage, Paging.of(dtoPage.getTotalPages(), page, size));

        model.addAttribute("quizzes", paged);
        log.info("Returning template 'quizzes/quizzes' with model");
        return "quizzes/quizzes";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizPage(Model model, @PathVariable("id") String id,
                                @Value("${citymonitor.maps.center.lat}") String mapCenterLat,
                                @Value("${citymonitor.maps.center.lng}") String mapCenterLng,
                                @Value("${citymonitor.maps.zoom}") String mapZoom){
        log.info("Getting quiz page with parameters: id = " + id);
        Quiz quiz = quizService.getById(Long.valueOf(id));
        QuizDTO quizDTO = quizConverter.convertEntityToDTO(quiz);
        model.addAttribute("quiz", quizDTO);

        if(quiz.getStatus().equals(QuizStatus.FINISHED)){
            model.addAttribute("mapCenterLat", mapCenterLat);
            model.addAttribute("mapCenterLng", mapCenterLng);
            model.addAttribute("mapZoom", mapZoom);
            List<Result> results = resultService.findResultByQuizId(Long.valueOf(id));
            List<ResultDTO> resultDTOs = resultConverter.convertListsEntityToDTO(results);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String resultsJSON = ow.writeValueAsString(resultDTOs);
                model.addAttribute("resultsJSON", resultsJSON);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        log.info("Returning template 'quizzes/quiz' with model");
        return "quizzes/quiz";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public String newQuiz(@ModelAttribute(name = "quiz") QuizDTO quiz, @ModelAttribute(name = "files") List<MultipartFile> files,
                          @ModelAttribute(name = "selectedLayoutId") String layoutId, Model model){
        log.info("Getting newQuiz page..");
        quiz.setStartImmediate(true);
        quiz.setOptionDTOs(List.of(new OptionDTO(), new OptionDTO()));
        model.addAttribute("layouts", layoutService.getNotDeprecatedLayouts());
        log.info("Returning template 'quizzes/newQuiz' with model");
        return "quizzes/newQuiz";
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public String saveQuiz(@Valid @ModelAttribute("quiz") QuizDTO quizDTO, BindingResult bindingResult,
                           @ModelAttribute("files") List<MultipartFile> files,
                           @ModelAttribute(name = "selectedLayoutId") String layoutId){
        log.info("Saving new quiz with parameters: quizDTO = " + quizDTO.toString() + "\nfiles = " + files.toString()
                + "\nselected layoutId = " + layoutId);
        if((!quizDTO.isStartImmediate() && Objects.isNull(quizDTO.getStartDate()))
                || bindingResult.hasErrors() || !isQuizPeriodCorrect(quizDTO) || layoutId == null) {
            throw new IllegalArgumentException();
        }

        List<FileDTO> fileDTOs = mFileConverter.convertListOfMultipartFileToDTO(files);
        quizDTO.setFileDTOs(fileDTOs);
        Layout layout = layoutService.getLayoutById(Long.valueOf(layoutId));

        if(layout.getStatus().equals(LayoutStatus.DEPRECATED)){
            log.error("Found DEPRECATED layout by provided layoutId");
            throw new IllegalArgumentException();
        }else {
            layout.setStatus(LayoutStatus.IN_USE);
            log.info("set layout status to IN_USE");
        }

        Quiz quiz = quizConverter.convertDTOToEntity(quizDTO);
        quiz.setLayout(layout);
        quizService.saveQuiz(quiz);
        log.info("Quiz successfully saved to database");

        if(quizDTO.isStartImmediate()){
            quizSender.sendImmediate(quiz);
            log.info("Quiz was sent to appropriate locals successfully");
        }else {
            quizSender.schedule(quiz);
            log.info("Quiz sending was successfully scheduled");
        }

        quizFinisher.schedule(quiz);
        log.info("Quiz finishing was successfully scheduled");

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public ResponseEntity removeQuiz(@PathVariable Long id){
        log.info("Removing quiz started: id = " + id.toString());
        Quiz quiz = quizService.getById(id);
        quizSender.removeScheduledSending(id);
        quizFinisher.removeScheduledFinish(id);
        quizService.deleteQuizById(id);
        //update layout status
        layoutService.markLayoutAsActual(quiz.getLayout().getId());
        log.info("Quiz with id = " + id + " removed successfully");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public ResponseEntity finishQuiz(@PathVariable(name = "id") Long id) {
        log.info("Finishing quiz with id = " + id);
        Quiz quiz = quizService.getById(id);
        quizFinisher.finishImmediate(quiz);
        log.info("Quiz with id " + id + " finished successfully");
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
