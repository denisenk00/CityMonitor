package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.entities.Result;

import java.util.Set;

public interface ResultsGenerator {

    Set<Result> generateResultsOfQuiz(Quiz quiz);

}
