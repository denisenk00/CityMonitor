package com.denysenko.citymonitorbot.services.entity;

import com.denysenko.citymonitorbot.models.Quiz;

import javax.validation.constraints.NotNull;

public interface QuizService {

    Quiz getQuizById(@NotNull Long quizId);
    boolean existsById(@NotNull Long quizId);

}
