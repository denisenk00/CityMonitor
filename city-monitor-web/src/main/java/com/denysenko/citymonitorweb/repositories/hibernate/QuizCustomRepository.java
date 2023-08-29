package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface QuizCustomRepository {

    Quiz getQuizWithOptionsAndFullLayoutById(@NotNull Long id);
    Quiz getFullQuizById(Long id);

}
