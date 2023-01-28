package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.answer.SaveAnswerCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.exceptions.NotAllowedQuizStatusException;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j
@Component
public class CallBackHandler implements Handler {

    @Autowired
    private BotUserService botUserService;
    @Autowired
    private SaveAnswerCommand saveAnswerCommand;
    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasCallbackQuery()) return false;
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long id = callbackQuery.getFrom().getId();
        Optional<BotStates> botUserState = botUserService.findBotStateByChatId(id);
        if(botUserState.isPresent()){
            return true;
        } else return false;
    }

    @Override
    public void handle(Update update) {
        log.info("Handled by CallBackHandler: updateId" + update.getUpdateId());
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        if(data.startsWith("answer:")){
            String [] parameters = data.replace("answer:", "").split(",");
            Long quizId = Long.valueOf(parameters[0].split("=")[1]);
            Long optionId = Long.valueOf(parameters[1].split("=")[1]);
            try {
                saveAnswerCommand.saveAnswer(chatId, quizId, optionId);
            }catch (NotAllowedQuizStatusException e){
                log.warn("Quiz " + quizId + " has already finished. Saving answers in unavailable");
                telegramService.sendAnswerCallbackQuery(callbackQuery.getId(), e.getMessage());
            }
            telegramService.sendAnswerCallbackQuery(callbackQuery.getId(), "Дякуємо за участь! Вашу відповідь збережено.");
        }
    }
}
