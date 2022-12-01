package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Result;

public interface ResultService {
    void saveResults(Iterable<Result> results);
    void deleteResultById(Long id);
}
