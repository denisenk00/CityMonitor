package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.SendAppealMenuCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class AppealHandler implements Handler {

    private static final Logger LOG = Logger.getLogger(AppealHandler.class);
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private SendAppealMenuCommand sendAppealMenuCommand;

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage()) return false;
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);
        if(botUserState.isPresent()){
            BotStates botState = botUserState.get();
            return botState.equals(BotStates.SEND_APPEAL_MENU);
        } else return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        LOG.info("Handled by AppealHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());

        sendAppealMenuCommand.saveAppeal(chatId, message);
    }
}
