package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;

import java.util.List;

public interface QuizService {
    List<QuizDTO> getFirstNQuizzes(int n);
    Paged<QuizDTO> getPageOfQuizzes(int pageNumber, int size);
    void saveQuiz(QuizDTO quizDTO);

}
