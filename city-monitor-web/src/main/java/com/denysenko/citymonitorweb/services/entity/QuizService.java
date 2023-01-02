package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface QuizService {

    List<Quiz> getLast10Quizzes();
    Page<Quiz> getPageOfQuizzes(int pageNumber, int size);
    void saveQuiz(@NotNull Quiz quiz);
    Quiz getById(@NotNull Long id);
    List<Quiz> findQuizzesByLayoutId(@NotNull Long id);
    void setQuizStatusById(@NotNull Long id, @NotNull QuizStatus quizStatus);
    void deleteQuizById(@NotNull Long quizId);

}
