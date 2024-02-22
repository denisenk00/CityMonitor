package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Quiz convertDTOToEntity(QuizDTO quizDTO) {
        try {
            Quiz quiz = new Quiz();
            quiz.setId(quizDTO.getId());
            quiz.setTitle(quizDTO.getTitle());
            quiz.setDescription(quizDTO.getDescription());
            quiz.setStartDate(quizDTO.getStartDate());
            quiz.setEndDate(quizDTO.getEndDate());
            quiz.setStatus(quizDTO.getStatus());

            optionConverter.convertListsDTOToEntity(quizDTO.getOptionDTOs())
                    .forEach(option -> quiz.addOption(option));

            fileConverter.convertListsDTOToEntity(quizDTO.getFileDTOs())
                    .forEach(file -> quiz.addFile(file));

            return quiz;
        } catch (Exception e) {
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
                    .endDate(quiz.getEndDate())
                    .status(quiz.getStatus());

            Optional.ofNullable(quiz.getLayout()).ifPresent(layout -> {
                LayoutDTO layoutDTO = layoutConverter.convertEntityToDTO(layout);
                quizDTOBuilder.layoutDTO(layoutDTO);
            });

            return quizDTOBuilder.build();
        } catch (Exception e) {
            throw new ConversionFailedException(TypeDescriptor.forObject(quiz), TypeDescriptor.valueOf(QuizDTO.class), null, e);
        }
    }
}
