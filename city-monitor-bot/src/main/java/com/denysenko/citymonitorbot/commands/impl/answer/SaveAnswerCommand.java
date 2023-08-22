package com.denysenko.citymonitorbot.commands.impl.answer;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.exceptions.NotAllowedQuizStatusException;
import com.denysenko.citymonitorbot.models.Answer;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.models.Quiz;
import com.denysenko.citymonitorbot.services.entity.AnswerService;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import com.denysenko.citymonitorbot.services.entity.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Component
public class SaveAnswerCommand implements Command<Long> {

    private final AnswerService answerService;
    private final BotUserService botUserService;
    private final QuizService quizService;

    @Transactional
    public void saveAnswer(Long chatId, Long quizId, Long optionId){
        final String UNAVAILABLE_QUIZ_MESSAGE = "Це опитування вже завершено. Чекаємо на вашу участь в наступних!";
        BotUser botUser = botUserService.getBotUserByChatId(chatId);
        Long userId = botUser.getBotUserId();

        if(!quizService.existsById(quizId))
            throw new NotAllowedQuizStatusException(UNAVAILABLE_QUIZ_MESSAGE);

        Quiz quiz = quizService.getQuizById(quizId);
        if(quiz.getStatus().equals("FINISHED"))
            throw new NotAllowedQuizStatusException(UNAVAILABLE_QUIZ_MESSAGE);

        Optional<Answer> oldAnswerOpt = answerService.findAnswerByQuizIdAndUserId(quizId, userId);
        oldAnswerOpt.ifPresentOrElse((oldAnswer) -> {
            if(!oldAnswer.getOptionId().equals(optionId)){
                oldAnswer.setOptionId(optionId);
                answerService.saveAnswer(oldAnswer);
            }
        }, () -> {
            Answer newAnswer = new Answer(userId, optionId, quiz);
            answerService.saveAnswer(newAnswer);
        });

    }

    @Override
    public void execute(Long chatId) {}
}
