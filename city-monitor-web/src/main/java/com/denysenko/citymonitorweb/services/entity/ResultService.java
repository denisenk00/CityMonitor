package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.dto.ResultDTO;
import com.denysenko.citymonitorweb.models.entities.Result;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface ResultService {
    void saveResults(@NotNull Iterable<Result> results);
    List<ResultDTO> findResultPreviewsByQuizId(@NotNull Long quizId);
}
