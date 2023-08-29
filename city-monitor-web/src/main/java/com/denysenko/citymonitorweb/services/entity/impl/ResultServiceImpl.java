package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.dto.ResultDTO;
import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.repositories.hibernate.ResultRepository;
import com.denysenko.citymonitorweb.services.entity.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    @Transactional
    public void saveResults(Iterable<Result> results){
        resultRepository.saveAll(results);
    }

    @Override
    public List<ResultDTO> findResultPreviewsByQuizId(Long quizId){
        return resultRepository.findAllResultPreviewsByOptionQuizId(quizId);
    }
}
