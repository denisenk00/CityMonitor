package com.denysenko.citymonitorweb.models.domain.async;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.QuizFinisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;

public class FinishQuizTask extends TimerTask {
    private Quiz quiz;
    private QuizFinisher quizFinisher;

    public FinishQuizTask(Quiz quiz, QuizFinisher quizFinisher) {
        this.quiz = quiz;
        this.quizFinisher = quizFinisher;
    }

    @Override
    public void run() {
        quizFinisher.finishImmediate(quiz);
    }
}
