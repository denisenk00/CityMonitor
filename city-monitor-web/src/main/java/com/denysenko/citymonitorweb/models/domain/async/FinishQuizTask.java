package com.denysenko.citymonitorweb.models.domain.async;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.QuizFinisher;
import lombok.AllArgsConstructor;

import java.util.TimerTask;

@AllArgsConstructor
public class FinishQuizTask extends TimerTask {
    private Quiz quiz;
    private QuizFinisher quizFinisher;

    @Override
    public void run() {
        quizFinisher.finishImmediate(quiz);
    }
}
