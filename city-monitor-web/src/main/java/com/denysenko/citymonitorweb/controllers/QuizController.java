package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.models.dto.*;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.QuizFinisher;
import com.denysenko.citymonitorweb.services.QuizSender;
import com.denysenko.citymonitorweb.services.converters.impl.MultiFileToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.entity.ResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Log4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/quizzes")
public class QuizController {
    @Value("${citymonitor.maps.center.lat}")
    private String mapCenterLat;
    @Value("${citymonitor.maps.center.lng}")
    private String mapCenterLng;
    @Value("${citymonitor.maps.zoom}")
    private String mapZoom;
    @Value("${citymonitor.googlemaps.apikey}")
    private String GOOGLE_MAPS_API_KEY;

    private final QuizService quizService;
    private final LayoutService layoutService;
    private final AppealService appealService;
    private final ResultService resultService;
    private final MultiFileToDTOConverter mFileConverter;
    private final QuizSender quizSender;
    private final QuizFinisher quizFinisher;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizzesPage(Model model,
                              @RequestParam(name = "page", defaultValue = "1", required = false) int pageNumber,
                              @RequestParam(name = "size", defaultValue = "30", required = false) int pageSize){
        log.info("Getting quizzes page with parameters: page = " + pageNumber + ", size = " + pageSize);
        if (pageNumber < 1 || pageSize < 1)
            throw new InputValidationException("Номер сторінки та її розмір має бути більше нуля. Поточні значення: pageNumber = " + pageNumber + ", pageSize = " + pageSize);

        Page<QuizPreviewDTO> quizzesPage = quizService.getPageOfQuizzesPreviews(pageNumber, pageSize);
        Paged<QuizPreviewDTO> paged = new Paged(quizzesPage, Paging.of(quizzesPage.getTotalPages(), pageNumber, pageSize));

        model.addAttribute("quizzes", paged);
        log.info("Returning template 'quizzes/quizzes' with model");
        return "quizzes/quizzes";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizPage(Model model, @PathVariable("id") Long id) throws JsonProcessingException {
        log.info("Getting quiz page with parameters: id = " + id);
        QuizDTO quizDTO;
        try {
            quizDTO = quizService.getFullDTOById(id);
        }catch (EntityNotFoundException e){
            throw new InputValidationException(e.getMessage(), e);
        }

        model.addAttribute("quiz", quizDTO);

        if(quizDTO.getStatus().equals(QuizStatus.FINISHED)){
            model.addAttribute("mapCenterLat", mapCenterLat);
            model.addAttribute("mapCenterLng", mapCenterLng);
            model.addAttribute("mapZoom", mapZoom);
            model.addAttribute("googlemaps_apikey", GOOGLE_MAPS_API_KEY);

            List<ResultDTO> results = resultService.findResultPreviewsByQuizId(id);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String resultsJSON = ow.writeValueAsString(results);
            model.addAttribute("resultsJSON", resultsJSON);
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
        model.addAttribute("layouts", layoutService.getNotDeprecatedLayoutsPreviews());
        log.info("Returning template 'quizzes/newQuiz' with model");
        return "quizzes/newQuiz";
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public String saveQuiz(@Valid @ModelAttribute("quiz") QuizDTO quizDTO,
                           @ModelAttribute("files") List<MultipartFile> files,
                           @ModelAttribute(name = "selectedLayoutId") Long layoutId){
        log.info("Saving new quiz with parameters: quizDTO = " + quizDTO.toString() + "\nfiles = " + files.toString()
                + "\nselected layoutId = " + layoutId);
        if(files.size() > 0 && !files.get(0).isEmpty()) {
            List<FileDTO> fileDTOs = mFileConverter.convertListOfMultipartFileToDTO(files);
            quizDTO.setFileDTOs(fileDTOs);
        }
        LayoutStatus layoutStatus = layoutService.getStatusById(layoutId);

        if(layoutStatus.equals(LayoutStatus.DEPRECATED)){
            log.error("Found DEPRECATED layout by provided layoutId");
            throw new InputValidationException("Обраний макет має статус 'Застарілий' і через це не може бути використаний для опитування.");
        }

        Quiz quiz = quizService.saveQuizWithLayout(quizDTO, layoutId);


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
        try {
            log.info("Removing quiz started: id = " + id.toString());
            Quiz quiz = quizService.getById(id);
            quizSender.removeScheduledSending(id);
            quizFinisher.removeScheduledFinish(id);
            quizService.deleteQuizById(id);
            layoutService.markLayoutAsActual(quiz.getLayout().getId());
            log.info("Quiz with id = " + id + " removed successfully");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
        }catch (EntityNotFoundException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAnyAuthority('quizzes:write')")
    public ResponseEntity finishQuiz(@PathVariable(name = "id") Long id) {
        try {
            log.info("Finishing quiz with id = " + id);
            Quiz quiz = quizService.getEntityWithFullLayoutAndOptionsById(id);
            quizFinisher.finishImmediate(quiz);
            log.info("Quiz with id " + id + " finished successfully");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
        }catch (EntityNotFoundException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
