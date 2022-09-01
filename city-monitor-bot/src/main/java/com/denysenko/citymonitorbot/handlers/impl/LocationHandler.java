package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.handlers.SuperUpdateHandler;
import com.denysenko.citymonitorbot.models.entities.BotUser;
import com.denysenko.citymonitorbot.models.entities.LocationPoint;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class LocationHandler implements Handler {
    private static final Logger LOG = Logger.getLogger(LocationHandler.class);
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private ProfileEnterLocationCommand profileEnterLocationCommand;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasLocation();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Location location = message.getLocation();
        Optional<BotStates> state = botUserService.findBotStateByChatId(chatId);

        if(state.isPresent() && state.get().equals(BotStates.EDITING_PROFILE_LOCATION)){
            LOG.info(location.getLatitude() + "  " + location.getLongitude() + " "  + location.getHorizontalAccuracy() + " "
            + location.getHeading() + " " + location.getLivePeriod() + " " + location.getProximityAlertRadius());
            profileEnterLocationCommand.saveLocation(chatId, location.getLatitude(), location.getLongitude());
            saveBotUserToDBAndRemoveCached(chatId);
        }
    }

    private void saveBotUserToDBAndRemoveCached(Long chatId){
        Optional<BotUser> cachedUser = botUserService.findBotUserInCacheByChatId(chatId);
        cachedUser.get().setActive(true);
        botUserService.saveBotUserToDB(cachedUser.get());
        botUserService.removeBotUserByChatIdFromCache(chatId);
    }

    //need to save user to db and clear cache
}
