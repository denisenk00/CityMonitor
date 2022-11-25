package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
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
    private FileEntityToDTOConverter fileConverter;
    @Autowired
    private OptionEntityToDTOConverter optionConverter;
    @Autowired
    private LayoutEntityToDTOConverter layoutConverter;
    @Autowired
    private QuizRepository quizRepository;


    @Override
    public Quiz convertDTOToEntity(QuizDTO quizDTO) {
        if(quizDTO == null) throw new IllegalArgumentException();
        Quiz quiz;
        if(quizDTO.getId() != null){
            Optional<Quiz> quizOptional = quizRepository.findById(quizDTO.getId());
            quiz = quizOptional.orElse(new Quiz());
        }else {
            quiz = new Quiz();
        }

        quiz.setId(quizDTO.getId());
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setFiles(fileConverter.convertListsDTOToEntity(quizDTO.getFileDTOs()));
        quiz.setOptions(optionConverter.convertListsDTOToEntity(quizDTO.getOptionDTOs()));
        quiz.setStartDate(quizDTO.getStartDate());
        quiz.setEndDate(quizDTO.getEndDate());

        if(Optional.ofNullable(quizDTO.getStatus()).isPresent()) {
            try {
                QuizStatus status = QuizStatus.getByTitle(quizDTO.getStatus());
                quiz.setStatus(status);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        Optional.ofNullable(quizDTO.getLayoutDTO())
                .ifPresent(layoutDTO -> quiz.setLayout(layoutConverter.convertDTOToEntity(layoutDTO)));

        return quiz;
    }

    @Override
    public QuizDTO convertEntityToDTO(Quiz quiz) {
        if(quiz == null) throw new IllegalArgumentException();

        QuizDTO.QuizDTOBuilder quizDTOBuilder = QuizDTO.builder();
        quizDTOBuilder
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .fileDTOs(fileConverter.convertListsEntityToDTO(quiz.getFiles()))
                .optionDTOs(optionConverter.convertListsEntityToDTO(quiz.getOptions()))
                .startDate(quiz.getStartDate())
                .endDate(quiz.getEndDate());

        Optional.ofNullable(quiz.getStatus())
                .ifPresent(quizStatus -> quizDTOBuilder.status(quizStatus.getTitle()));
        Optional.ofNullable(quiz.getLayout()).ifPresent(layout -> {
            LayoutDTO layoutDTO = layoutConverter.convertEntityToDTO(layout);
            quizDTOBuilder.layoutDTO(layoutDTO);
        });

        return quizDTOBuilder.build();
    }
}
