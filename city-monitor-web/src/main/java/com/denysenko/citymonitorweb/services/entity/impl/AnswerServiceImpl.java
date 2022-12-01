package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.entities.Answer;
import com.denysenko.citymonitorweb.repositories.hibernate.AnswerRepository;
import com.denysenko.citymonitorweb.services.entity.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    public List<Answer> getAnswersByQuizId(Long quizId){
        if(quizId == null) throw new IllegalArgumentException();

        return answerRepository.findAllByOptionQuizId(quizId);
    }

    public void deleteAnswersByQuizId(Long quizId){
        if(quizId == null) throw new IllegalArgumentException();
        answerRepository.deleteAllByOptionQuizId(quizId);
    }

}
