package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizEntityToDTOConverter implements EntityDTOConverter<Quiz, QuizDTO> {
    @Autowired
    @Qualifier("fileEntityToDTOConverter")
    private EntityDTOConverter fileConverter;
    @Autowired
    @Qualifier("optionEntityToDTOConverter")
    private EntityDTOConverter optionConverter;
    @Autowired
    private LayoutService layoutService;


    @Override
    public Quiz convertDTOToEntity(QuizDTO quizDTO) {
        Quiz.QuizBuilder quizBuilder = Quiz.builder();
        quizBuilder
                .id(quizDTO.getId())
                .title(quizDTO.getTitle())
                .files(fileConverter.convertListsDTOToEntity(quizDTO.getFileDTOs()))
                .options(optionConverter.convertListsDTOToEntity(quizDTO.getOptionDTOs()))
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
                .id(quiz.getId())
                .title(quiz.getTitle())
                .fileDTOs(fileConverter.convertListsEntityToDTO(quiz.getFiles()))
                .optionDTOs(optionConverter.convertListsEntityToDTO(quiz.getOptions()))
                .startDate(quiz.getStartDate())
                .endDate(quiz.getEndDate());

        Optional.ofNullable(quiz.getStatus())
                .ifPresent(quizStatus -> quizDTOBuilder.status(quizStatus.getTitle()));
        Optional.ofNullable(quiz.getLayout()).ifPresent(layout -> quizDTOBuilder.layoutId(layout.getId()));

        return quizDTOBuilder.build();
    }
}
