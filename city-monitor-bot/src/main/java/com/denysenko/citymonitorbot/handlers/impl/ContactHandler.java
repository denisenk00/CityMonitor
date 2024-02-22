package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Component
public class ContactHandler implements Handler {

    private final ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    private final ProfileEnterLocationCommand profileEnterLocationCommand;
    private final CacheManager cacheManager;

    @Override
    public boolean isApplicable(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasContact()) return false;
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = cacheManager.findBotStateByChatId(chatId);
        if (botUserState.isPresent()) {
            BotStates botState = botUserState.get();
            return botState.equals(BotStates.EDITING_PROFILE_PHONE);
        } else return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        log.info("Handled update by ContactHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());

        Contact contact = message.getContact();
        profileEnterPhoneNumberCommand.savePhoneNumber(chatId, contact.getPhoneNumber());
        profileEnterLocationCommand.execute(chatId);
    }
}
