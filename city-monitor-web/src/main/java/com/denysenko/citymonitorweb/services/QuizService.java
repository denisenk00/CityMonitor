package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.Quiz;
import com.denysenko.citymonitorweb.models.paging.Paged;

import java.util.List;

public interface QuizService {
    List<Quiz> getFirstNQuizzes(int n);
    Paged<Quiz> getPageOfQuizzes(int pageNumber, int size);

}
