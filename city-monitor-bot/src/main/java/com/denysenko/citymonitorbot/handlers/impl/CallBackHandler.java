package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class CallBackHandler implements Handler {

    private static final Logger LOG = Logger.getLogger(CallBackHandler.class);
    @Autowired
    private BotUserService botUserService;

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
        LOG.info("Handled by CallBackHandler: updateId" + update.getUpdateId());
    }
}
