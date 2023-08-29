package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.dto.QuizPreviewDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<QuizPreviewDTO> findAllPreviewsByLayoutId(Long layoutId);
    boolean existsByLayoutId(Long layoutId);
    Page<QuizPreviewDTO> findAllPreviewsBy(PageRequest pageRequest);
    List<QuizPreviewDTO> findFirst10PreviewsByOrderByStartDateDesc();

}
