package com.denysenko.citymonitorbot.repositories.hibernate;

import com.denysenko.citymonitorbot.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
