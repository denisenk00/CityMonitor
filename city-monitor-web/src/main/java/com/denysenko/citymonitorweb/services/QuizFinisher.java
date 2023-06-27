package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface QuizFinisher {

    void finishImmediate(@NotNull Quiz quiz);
    void schedule(@NotNull Quiz quiz);
    void removeScheduledFinish(@NotNull Long quizId);

}
