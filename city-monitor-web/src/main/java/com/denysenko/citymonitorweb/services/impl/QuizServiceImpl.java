package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.*;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

@Service
public class QuizServiceImpl implements QuizService, EntityDTOConverter<Quiz, QuizDTO> {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private LayoutService layoutService;

    @Override
    public List<QuizDTO> getFirstNQuizzes(int n) {
        return null;
    }

    @Override
    public Paged<QuizDTO> getPageOfQuizzes(int pageNumber, int size){
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Quiz> quizzesPage = quizRepository.findAll(request);
        Page<QuizDTO> dtoPage = quizzesPage.map(quiz -> convertEntityToDTO(quiz));
        return new Paged<QuizDTO>(dtoPage, Paging.of(dtoPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public void saveQuiz(QuizDTO quizDTO){
        Quiz quiz = convertDTOToEntity(quizDTO);
        quizRepository.save(quiz);
    }

    @Override
    public Quiz convertDTOToEntity(QuizDTO quizDTO) {
        Quiz.QuizBuilder quizBuilder = Quiz.builder();
        quizBuilder
                .quizId(quizDTO.getId())
                .title(quizDTO.getTitle())
                .files(fileService.convertListsDTOToEntity(quizDTO.getFileDTOs()))
                .options(optionService.convertListsDTOToEntity(quizDTO.getOptionDTOs()))
                .startDate(quizDTO.getStartDate())
                .endDate(quizDTO.getEndDate());
        if(Optional.ofNullable(quizDTO.getStatus()).isPresent()) {
            try {
                quizBuilder.status(QuizStatus.getByTitle(quizDTO.getStatus()));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        Optional.ofNullable(quizDTO.getLayoutId())
                .ifPresent(layoutId -> quizBuilder.layout(layoutService.getLayoutById(layoutId)));

        return quizBuilder.build();
    }

    @Override
    public QuizDTO convertEntityToDTO(Quiz quiz) {
        QuizDTO.QuizDTOBuilder quizDTOBuilder = QuizDTO.builder();
        quizDTOBuilder
                .id(quiz.getQuizId())
                .title(quiz.getTitle())
                .fileDTOs(fileService.convertListsEntityToDTO(quiz.getFiles()))
                .optionDTOs(optionService.convertListsEntityToDTO(quiz.getOptions()))
                .startDate(quiz.getStartDate())
                .endDate(quiz.getEndDate());

        Optional.ofNullable(quiz.getStatus())
                .ifPresent(quizStatus -> quizDTOBuilder.status(quizStatus.getTitle()));
        Optional.ofNullable(quiz.getLayout()).ifPresent(layout -> quizDTOBuilder.layoutId(layout.getLayoutId()));

        return quizDTOBuilder.build();
    }
}
