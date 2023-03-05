package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorbot.models.Quiz;
import com.denysenko.citymonitorbot.repositories.hibernate.QuizRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Log4j
@Service
@Validated
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz getQuizById(@NotNull Long quizId){
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz with id = " + quizId + " was not found"));
    }

    public boolean existsById(@NotNull Long quizId){
        return quizRepository.existsById(quizId);
    }
}
