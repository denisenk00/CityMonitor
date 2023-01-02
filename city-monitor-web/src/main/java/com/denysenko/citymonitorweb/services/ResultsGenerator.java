package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.entities.Result;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Validated
public interface ResultsGenerator {

    Set<Result> generateResultsOfQuiz(@NotNull Quiz quiz);

}
