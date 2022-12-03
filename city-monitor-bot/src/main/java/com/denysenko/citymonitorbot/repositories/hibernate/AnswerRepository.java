package com.denysenko.citymonitorbot.repositories.hibernate;

import com.denysenko.citymonitorbot.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByQuizIdAndLocalId(Long quizId, Long localId);

}
