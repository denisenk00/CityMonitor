package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.entities.Answer;
import com.denysenko.citymonitorweb.repositories.hibernate.AnswerRepository;
import com.denysenko.citymonitorweb.services.entity.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    public List<Answer> getAnswersByQuizId(Long quizId){
        return answerRepository.findAllByOptionQuizId(quizId);
    }

    @Transactional
    public void deleteAnswersByQuizId(Long quizId){
        answerRepository.deleteAllByOptionQuizId(quizId);
    }

}
