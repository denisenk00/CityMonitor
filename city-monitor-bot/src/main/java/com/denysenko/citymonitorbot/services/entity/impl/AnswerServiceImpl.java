package com.denysenko.citymonitorbot.services.entity.impl;

import com.denysenko.citymonitorbot.models.Answer;
import com.denysenko.citymonitorbot.repositories.hibernate.AnswerRepository;
import com.denysenko.citymonitorbot.services.entity.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public void saveAnswer(@NotNull Answer answer){
        answerRepository.save(answer);
    }

    public Optional<Answer> findAnswerByQuizIdAndUserId(@NotNull Long quizId, @NotNull Long userId){
        return answerRepository.findByQuizIdAndLocalId(quizId, userId);
    }

}
