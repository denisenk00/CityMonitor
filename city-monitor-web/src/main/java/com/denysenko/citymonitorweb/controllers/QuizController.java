package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    public String quizzesPage(Model model,
                              @RequestParam(name = "page", defaultValue = "1", required = false) int pageNumber,
                              @RequestParam(name = "size", defaultValue = "30", required = false) int pageSize){
        log.info("Getting quizzes page with parameters: page = " + pageNumber + ", size = " + pageSize);
        if (pageNumber < 1 || pageSize < 1)
            throw new InputValidationException("Номер сторінки та її розмір має бути більше нуля. Поточні значення: pageNumber = " + pageNumber + ", pageSize = " + pageSize);

        Page<Quiz> quizzesPage = quizService.getPageOfQuizzes(pageNumber, pageSize);
        Page<QuizDTO> dtoPage = quizzesPage.map(quiz -> quizConverter.convertEntityToDTO(quiz));
        Paged<QuizDTO> paged = new Paged(dtoPage, Paging.of(dtoPage.getTotalPages(), pageNumber, pageSize));

        model.addAttribute("quizzes", paged);
        log.info("Returning template 'quizzes/quizzes' with model");
        return "quizzes/quizzes";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public String quizPage(Model model, @PathVariable("id") Long id) throws JsonProcessingException {
        log.info("Getting quiz page with parameters: id = " + id);
        Quiz quiz;
        try {
            quiz = quizService.getById(id);
        }catch (EntityNotFoundException e){
            throw new InputValidationException(e.getMessage(), e);
        }

        QuizDTO quizDTO = quizConverter.convertEntityToDTO(quiz);
        model.addAttribute("quiz", quizDTO);

        if(quiz.getStatus().equals(QuizStatus.FINISHED)){
            model.addAttribute("mapCenterLat", mapCenterLat);
            model.addAttribute("mapCenterLng", mapCenterLng);
            model.addAttribute("mapZoom", mapZoom);
            model.addAttribute("googlemaps_apikey", GOOGLE_MAPS_API_KEY);
            List<Result> results = resultService.findResultByQuizId(id);
            List<ResultDTO> resultDTOs = resultConverter.convertListsEntityToDTO(results);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String resultsJSON = ow.writeValueAsString(resultDTOs);
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
        model.addAttribute("layouts", layoutService.getNotDeprecatedLayouts());
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
        Layout layout = layoutService.getLayoutById(layoutId);

        if(layout.getStatus().equals(LayoutStatus.DEPRECATED)){
            log.error("Found DEPRECATED layout by provided layoutId");
            throw new InputValidationException("Обраний макет має статус 'Застарілий' і через це не може бути використаний для опитування.");
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
            Quiz quiz = quizService.getById(id);
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
