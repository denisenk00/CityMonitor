package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Answer;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface AnswerService {
    List<Answer> getAnswersByQuizId(@NotNull Long quizId);
    void deleteAnswersByQuizId(@NotNull Long quizId);
}
