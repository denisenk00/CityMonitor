package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface QuizSender {

    void schedule(@NotNull Quiz quiz);
    void sendImmediate(@NotNull Quiz quiz);
    void removeScheduledSending(@NotNull Long quizId);

}
