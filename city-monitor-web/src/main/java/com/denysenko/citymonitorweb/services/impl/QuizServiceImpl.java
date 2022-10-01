package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
    public List<Quiz> getFirstNQuizzes(int n) {
        return null;
    }

    public Paged<Quiz> getPageOfQuizzes(int pageNumber, int size){
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Quiz> postPage = quizRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public Quiz convertDTOToEntity(QuizDTO quizDTO) throws Exception {
        Quiz.QuizBuilder quizBuilder = Quiz.builder();
        quizBuilder
                .quizId(quizDTO.getId())
                .title(quizDTO.getTitle())
                .files(fileService.convertListsDTOToEntity(quizDTO.getFileDTOs()))
                .options(optionService.convertListsDTOToEntity(quizDTO.getOptionDTOs()));
        if(Optional.ofNullable(quizDTO.getStatus()).isPresent())
            quizBuilder.status(QuizStatus.getByTitle(quizDTO.getStatus()));
        Optional.ofNullable(quizDTO.getStartDate())
                .ifPresent(startDate -> quizBuilder.startDate(LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault())));
        Optional.ofNullable(quizDTO.getEndDate())
                .ifPresent(endDate -> LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()));
        Optional.ofNullable(quizDTO.getLayoutId())
                .ifPresent(layoutId -> quizBuilder.layout(layoutService.getLayoutById(layoutId)));

        return quizBuilder.build();
    }

    @Override
    public QuizDTO convertEntityToDTO(Quiz quiz) throws Exception {
        QuizDTO.QuizDTOBuilder quizDTOBuilder = QuizDTO.builder();
        quizDTOBuilder
                .id(quiz.getQuizId())
                .title(quiz.getTitle())
                .fileDTOs(fileService.convertListsEntityToDTO(quiz.getFiles()))
                .optionDTOs(optionService.convertListsEntityToDTO(quiz.getOptions()));

        Optional.ofNullable(quiz.getStatus())
                .ifPresent(quizStatus -> quizDTOBuilder.status(quizStatus.getTitle()));
        Optional.ofNullable(quiz.getStartDate())
                .ifPresent(startDate -> quizDTOBuilder.startDate(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant())));
        quizDTOBuilder.endDate(Date.from(quiz.getStartDate().atZone(ZoneId.systemDefault()).toInstant()))
                .layoutId(quiz.getLayout().getLayoutId());
        Optional.ofNullable(quiz.getLayout()).ifPresent(layout -> quizDTOBuilder.layoutId(layout.getLayoutId()));

        return quizDTOBuilder.build();
    }
}
