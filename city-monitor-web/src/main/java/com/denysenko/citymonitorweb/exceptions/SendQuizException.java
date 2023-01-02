package com.denysenko.citymonitorweb.exceptions;

import com.denysenko.citymonitorweb.models.entities.Quiz;

import java.util.Collection;

public class SendQuizException extends RuntimeException {
    private Quiz quiz;
    private Collection<Long> chatIds;

    public SendQuizException(Quiz quiz, Collection<Long> chatIds) {
        this.quiz = quiz;
        this.chatIds = chatIds;
    }

    public SendQuizException(String message, Quiz quiz, Collection<Long> chatIds) {
        super(message);
        this.quiz = quiz;
        this.chatIds = chatIds;
    }

    public SendQuizException(String message, Throwable cause, Quiz quiz, Collection<Long> chatIds) {
        super(message, cause);
        this.quiz = quiz;
        this.chatIds = chatIds;
    }

    public SendQuizException(Throwable cause, Quiz quiz, Collection<Long> chatIds) {
        super(cause);
        this.quiz = quiz;
        this.chatIds = chatIds;
    }
}

