package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.domain.async.FinishQuizTask;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.entity.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizFinisherImpl implements QuizFinisher {

    private Map<Long, Timer> scheduledTasks = new HashMap<>();
    @Autowired
    private QuizService quizService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private ResultsGenerator resultsGenerator;

    @Override
    public void finishImmediate(Quiz quiz) {
        if(quiz == null) throw new IllegalArgumentException();
        removeScheduledFinish(quiz.getId());

        Set<Result> resultSet = resultsGenerator.generateResultsOfQuiz(quiz);
        resultService.saveResults(resultSet);

        quizService.setQuizStatusById(quiz.getId(), QuizStatus.FINISHED);
    }

    @Override
    public void schedule(Quiz quiz) {
        if(quiz == null) throw new IllegalArgumentException();

        FinishQuizTask finishQuizTask = new FinishQuizTask(quiz);
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(quiz.getEndDate().getYear(), quiz.getEndDate().getMonthValue() - 1, quiz.getEndDate().getDayOfMonth(),
                quiz.getEndDate().getHour(), quiz.getEndDate().getMinute(), quiz.getEndDate().getSecond());
        timer.schedule(finishQuizTask, calendar.getTime());
        scheduledTasks.put(quiz.getId(), timer);
    }

    public void removeScheduledFinish(Long quizId){
        if (quizId == null) throw new IllegalArgumentException();

        Timer timer = scheduledTasks.get(quizId);
        if(timer != null){
            timer.cancel();
            scheduledTasks.remove(quizId);
        }
    }

}
