package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.dto.QuizPreviewDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface QuizService {

    List<QuizPreviewDTO> getLast10QuizzesPreviews();
    Page<QuizPreviewDTO> getPageOfQuizzesPreviews(int pageNumber, int size);
    Quiz saveQuizWithLayout(QuizDTO quizDTO, Long layoutId);
    Quiz getById(@NotNull Long id);
    Quiz getEntityWithFullLayoutAndOptionsById(@NotNull Long id);
    QuizDTO getFullDTOById(@NotNull Long id);
    List<QuizPreviewDTO> findQuizzesPreviewsByLayoutId(@NotNull Long id);
    void setQuizStatusById(@NotNull Long id, @NotNull QuizStatus quizStatus);
    void deleteQuizById(@NotNull Long quizId);

}
