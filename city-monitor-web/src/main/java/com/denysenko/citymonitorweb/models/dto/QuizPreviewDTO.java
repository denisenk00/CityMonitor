package com.denysenko.citymonitorweb.models.dto;

import com.denysenko.citymonitorweb.enums.QuizStatus;

import java.time.LocalDateTime;

public interface QuizPreviewDTO {
    Long getId();
    String getTitle();
    String getDescription();
    QuizStatus getStatus();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    LayoutPreviewDTO getLayout();
}
