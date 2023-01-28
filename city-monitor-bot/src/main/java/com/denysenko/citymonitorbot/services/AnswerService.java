package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.Answer;
import com.denysenko.citymonitorbot.repositories.hibernate.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public void saveAnswer(@NotNull Answer answer){
        answerRepository.save(answer);
    }

    public Optional<Answer> findAnswerByQuizIdAndUserId(@NotNull Long quizId, @NotNull Long userId){
        return answerRepository.findByQuizIdAndLocalId(quizId, userId);
    }

}
