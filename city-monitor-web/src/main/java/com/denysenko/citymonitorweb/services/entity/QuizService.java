package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.entities.Quiz;

import java.util.List;

public interface QuizService {
    List<Quiz> getLast10Quizzes();
    Paged<QuizDTO> getPageOfQuizzes(int pageNumber, int size);
    void saveQuiz(Quiz quiz);

}
