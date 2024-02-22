package com.denysenko.citymonitorweb.models.dto;

public interface ResultDTO {
    Long getId();
    ReadOptionDTO getOption();
    Long getPolygonId();
    Integer getAnswersCount();

    interface ReadOptionDTO {
        Long getId();
        String getTitle();
    }
}
