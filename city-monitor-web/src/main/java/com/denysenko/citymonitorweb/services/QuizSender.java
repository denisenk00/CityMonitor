package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;

public interface QuizSender {
    void schedule(Quiz quiz);
    void sendImmediate(Quiz quiz);
    void removeScheduledQuiz(Long quizId);
}
