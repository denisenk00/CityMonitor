package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.Answer;
import com.denysenko.citymonitorbot.repositories.hibernate.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void saveAnswer(@NotNull Answer answer){
        answerRepository.save(answer);
    }

    public Optional<Answer> findAnswerByQuizIdAndUserId(@NotNull Long quizId, @NotNull Long userId){
        return answerRepository.findByQuizIdAndLocalId(quizId, userId);
    }

}
