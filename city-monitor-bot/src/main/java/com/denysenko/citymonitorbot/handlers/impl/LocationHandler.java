package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Component
public class LocationHandler implements Handler {

    private final CacheManager cacheManager;
    private final BotUserService botUserService;
    private final ProfileEnterLocationCommand profileEnterLocationCommand;
    private final AppealEnterLocationCommand appealEnterLocationCommand;
    private final MainMenuCommand mainMenuCommand;

    @Override
    public boolean isApplicable(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasLocation()) return false;
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = cacheManager.findBotStateByChatId(chatId);
        if (botUserState.isPresent()) {
            BotStates botState = botUserState.get();
            return botState.equals(BotStates.EDITING_PROFILE_LOCATION) || botState.equals(BotStates.APPEAL_ENTERING_LOCATION);
        } else return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        log.info("Handled update by LocationHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());

        Location location = message.getLocation();
        BotStates botUserState = cacheManager.findBotStateByChatId(chatId).get();
        if (botUserState.equals(BotStates.EDITING_PROFILE_LOCATION)) {
            profileEnterLocationCommand.saveLocation(chatId, location.getLongitude(), location.getLatitude());
            botUserService.saveToDBAndCleanCache(chatId);
            mainMenuCommand.execute(chatId);
        } else if (botUserState.equals(BotStates.APPEAL_ENTERING_LOCATION)) {
            appealEnterLocationCommand.saveLocation(chatId, location.getLongitude(), location.getLatitude());
        }
    }

}
