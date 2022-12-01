package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.*;
import com.denysenko.citymonitorweb.services.entity.AnswerService;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResultsGeneratorImpl implements ResultsGenerator{
    @Autowired
    private AnswerService answerService;

    public Set<Result> generateResultsOfQuiz(Quiz quiz){
        Set<Result> results = new HashSet<>();
        List<Polygon> polygons = quiz.getLayout().getPolygons();
        List<Option> options = quiz.getOptions();
        List<Answer> answers = answerService.getAnswersByQuizId(quiz.getId());

        for(Polygon polygon : polygons){
            for(Option option : options){
                results.add(new Result(option, polygon, 0));
            }
        }

        for(Answer answer : answers){
            for(Result result : results){
                Option answerOption = answer.getOption();
                Point localPoint = answer.getLocal().getLocation();

                if(answerOption.equals(result.getOption()) && result.getPolygon().getPolygon().contains(localPoint)){
                    result.setAnswersCount(result.getAnswersCount() + 1);
                }
            }
        }

        return results;
    }

}
