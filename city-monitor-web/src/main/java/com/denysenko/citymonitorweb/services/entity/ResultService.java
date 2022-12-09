package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Result;

import java.util.List;

public interface ResultService {
    void saveResults(Iterable<Result> results);
    List<Result> findResultByQuizId(Long quizId);
}
