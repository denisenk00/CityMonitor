package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizEntityToDTOConverter implements EntityDTOConverter<Quiz, QuizDTO> {

    private final FileEntityToDTOConverter fileConverter;
    private final OptionEntityToDTOConverter optionConverter;
    private final LayoutEntityToDTOConverter layoutConverter;
    private final QuizRepository quizRepository;


    @Override
    public Quiz convertDTOToEntity(QuizDTO quizDTO) {
        try {
            Quiz quiz;
            if (quizDTO.getId() != null) {
                Optional<Quiz> quizOptional = quizRepository.findById(quizDTO.getId());
                quiz = quizOptional.orElse(new Quiz());
            } else {
                quiz = new Quiz();
            }

            quiz.setId(quizDTO.getId());
            quiz.setTitle(quizDTO.getTitle());
            quiz.setDescription(quizDTO.getDescription());
            quiz.setFiles(fileConverter.convertListsDTOToEntity(quizDTO.getFileDTOs()));
            quiz.setOptions(optionConverter.convertListsDTOToEntity(quizDTO.getOptionDTOs()));
            quiz.setStartDate(quizDTO.getStartDate());
            quiz.setEndDate(quizDTO.getEndDate());

            if (Optional.ofNullable(quizDTO.getStatus()).isPresent()) {
                QuizStatus status = QuizStatus.getByTitle(quizDTO.getStatus());
                quiz.setStatus(status);
            }
            Optional.ofNullable(quizDTO.getLayoutDTO())
                    .ifPresent(layoutDTO -> quiz.setLayout(layoutConverter.convertDTOToEntity(layoutDTO)));

            return quiz;
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(quizDTO), TypeDescriptor.valueOf(Quiz.class), null, e);
        }
    }

    @Override
    public QuizDTO convertEntityToDTO(Quiz quiz) {
        try {
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
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(quiz), TypeDescriptor.valueOf(QuizDTO.class), null, e);
        }
    }
}
