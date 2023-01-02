package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.entities.Answer;
import com.denysenko.citymonitorweb.repositories.hibernate.AnswerRepository;
import com.denysenko.citymonitorweb.services.entity.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    public List<Answer> getAnswersByQuizId(Long quizId){
        return answerRepository.findAllByOptionQuizId(quizId);
    }

    @Transactional
    public void deleteAnswersByQuizId(Long quizId){
        answerRepository.deleteAllByOptionQuizId(quizId);
    }

}
