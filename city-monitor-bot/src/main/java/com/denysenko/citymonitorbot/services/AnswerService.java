package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.Answer;
import com.denysenko.citymonitorbot.repositories.hibernate.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public void saveAnswer(Answer answer){
        if(answer == null) throw new IllegalArgumentException("Answer should not be NULL");
        answerRepository.save(answer);
    }

    public Optional<Answer> findAnswerByQuizIdAndUserId(Long quizId, Long userId){
        if(quizId == null) throw new IllegalArgumentException();
        if(userId == null) throw new IllegalArgumentException();
        return answerRepository.findByQuizIdAndLocalId(quizId, userId);
    }

}
