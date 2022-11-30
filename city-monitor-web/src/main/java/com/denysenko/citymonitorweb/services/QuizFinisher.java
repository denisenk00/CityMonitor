package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;

public interface QuizFinisher {
    void finishImmediate(Quiz quiz);
    void schedule(Quiz quiz);
    void removeScheduledFinish(Long quizId);
}
