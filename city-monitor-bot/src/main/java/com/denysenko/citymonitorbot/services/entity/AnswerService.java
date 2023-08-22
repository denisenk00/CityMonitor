package com.denysenko.citymonitorbot.services.entity;

import com.denysenko.citymonitorbot.models.Answer;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface AnswerService {

    void saveAnswer(@NotNull Answer answer);
    Optional<Answer> findAnswerByQuizIdAndUserId(@NotNull Long quizId, @NotNull Long userId);

}
