package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.repositories.hibernate.ResultRepository;
import com.denysenko.citymonitorweb.services.entity.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public void saveResults(Iterable<Result> results){
        resultRepository.saveAll(results);
    }

    public List<Result> findResultByQuizId(Long quizId){
        return resultRepository.findAllByOptionQuizId(quizId);
    }
}
