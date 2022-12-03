package com.denysenko.citymonitorweb.models.domain.async;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.QuizSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;

public class SendQuizTask extends TimerTask {
    private QuizSender quizSender;
    private Quiz quiz;

    public SendQuizTask(Quiz quiz, QuizSender quizSender) {
        this.quizSender = quizSender;
        this.quiz = quiz;
    }

    @Override
    public void run() {
        quizSender.sendImmediate(quiz);
    }
}
