package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.domain.async.FinishQuizTask;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.services.entity.AnswerService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.entity.ResultService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j
@Service
public class QuizFinisherImpl implements QuizFinisher {

    private Map<Long, Timer> scheduledTasks = new HashMap<>();
    @Autowired
    private QuizService quizService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private ResultsGenerator resultsGenerator;
    @Autowired
    private AnswerService answerService;

    @Transactional
    @Override
    public void finishImmediate(Quiz quiz) {
        log.info("finishing quiz with id = " + quiz.getId() + " immediately");
        removeScheduledFinish(quiz.getId());

        Set<Result> resultSet = resultsGenerator.generateResultsOfQuiz(quiz);
        resultService.saveResults(resultSet);
        answerService.deleteAnswersByQuizId(quiz.getId());

        quizService.setQuizStatusById(quiz.getId(), QuizStatus.FINISHED);
    }

    @Transactional
    @Override
    public void schedule(Quiz quiz) {
        log.info("scheduling quiz finish with id = " + quiz.getId());
        FinishQuizTask finishQuizTask = new FinishQuizTask(quiz, this);
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(quiz.getEndDate().getYear(), quiz.getEndDate().getMonthValue() - 1, quiz.getEndDate().getDayOfMonth(),
                quiz.getEndDate().getHour(), quiz.getEndDate().getMinute(), quiz.getEndDate().getSecond());
        timer.schedule(finishQuizTask, calendar.getTime());
        scheduledTasks.put(quiz.getId(), timer);
    }

    public void removeScheduledFinish(Long quizId){
        log.info("removing quiz finish from scheduled if exists: id = " + quizId);
        Timer timer = scheduledTasks.get(quizId);
        if(timer != null){
            timer.cancel();
            scheduledTasks.remove(quizId);
        }
    }

}
