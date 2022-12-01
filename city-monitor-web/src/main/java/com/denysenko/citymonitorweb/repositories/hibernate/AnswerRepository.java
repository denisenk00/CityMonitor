package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByOptionQuizId(Long quizId);
    void deleteAllByOptionQuizId(Long quizId);
}
