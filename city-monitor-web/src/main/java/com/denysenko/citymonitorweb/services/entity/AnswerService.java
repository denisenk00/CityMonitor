package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> getAnswersByQuizId(Long quizId);
}
