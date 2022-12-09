package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findAllByOptionQuizId(Long quizId);
}
