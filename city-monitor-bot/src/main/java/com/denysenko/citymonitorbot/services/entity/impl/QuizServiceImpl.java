package com.denysenko.citymonitorbot.services.entity.impl;

import com.denysenko.citymonitorbot.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorbot.models.Quiz;
import com.denysenko.citymonitorbot.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorbot.services.entity.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Log4j
@RequiredArgsConstructor
@Service
@Validated
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    public Quiz getQuizById(@NotNull Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz with id = " + quizId + " was not found"));
    }

    public boolean existsById(@NotNull Long quizId) {
        return quizRepository.existsById(quizId);
    }
}
