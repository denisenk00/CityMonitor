package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;

import java.util.List;

public interface QuizService {
    List<Quiz> getFirstNQuizzes(int n);
    Paged<Quiz> getPageOfQuizzes(int pageNumber, int size);

}
