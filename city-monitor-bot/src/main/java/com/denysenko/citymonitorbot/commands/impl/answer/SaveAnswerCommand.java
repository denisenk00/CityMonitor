package com.denysenko.citymonitorbot.commands.impl.answer;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.exceptions.NotAllowedQuizStatusException;
import com.denysenko.citymonitorbot.models.Answer;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.models.Quiz;
import com.denysenko.citymonitorbot.services.AnswerService;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.QuizService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j
@Component
public class SaveAnswerCommand implements Command<Long> {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private QuizService quizService;

    public void saveAnswer(Long chatId, Long quizId, Long optionId){
        BotUser botUser = botUserService.getBotUserByChatId(chatId);
        Long userId = botUser.getBotUserId();

        Quiz quiz = quizService.getQuizById(quizId);
        if(quiz.getStatus().equals("FINISHED"))
            throw new NotAllowedQuizStatusException("Це опитування вже завершено. Чекаємо на вашу участь в наступних!");

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
