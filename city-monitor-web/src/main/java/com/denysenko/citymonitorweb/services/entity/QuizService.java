package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuizService {

    List<Quiz> getLast10Quizzes();
    Page<Quiz> getPageOfQuizzes(int pageNumber, int size);
    void saveQuiz(Quiz quiz);
    Quiz getById(Long id);
    List<Quiz> findQuizzesByLayoutId(Long id);
    void setQuizStatusById(Long id, QuizStatus quizStatus);

}
