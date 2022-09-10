package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class LocationHandler implements Handler {

    private static final Logger LOG = Logger.getLogger(LocationHandler.class);
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private ProfileEnterLocationCommand profileEnterLocationCommand;
    @Autowired
    private AppealEnterLocationCommand appealEnterLocationCommand;

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasLocation()) return false;
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);
        if(botUserState.isPresent()){
            BotStates botState = botUserState.get();
            return botState.equals(BotStates.EDITING_PROFILE_LOCATION) || botState.equals(BotStates.APPEAL_ENTERING_LOCATION);
        } else return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        LOG.info("Handled update by LocationHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());

        Location location = message.getLocation();
        BotStates botUserState = botUserService.findBotStateByChatId(chatId).get();
        if(botUserState.equals(BotStates.EDITING_PROFILE_LOCATION)){
            profileEnterLocationCommand.saveLocation(chatId, location.getLatitude(), location.getLongitude());
        }else if(botUserState.equals(BotStates.APPEAL_ENTERING_LOCATION)){
            appealEnterLocationCommand.saveLocation(chatId, location.getLatitude(), location.getLongitude());
        }
    }
}
