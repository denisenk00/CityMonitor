package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class ContactHandler implements Handler {
    @Autowired
    private ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    @Autowired
    private BotUserService botUserService;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasContact();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Contact contact = message.getContact();
        Optional<BotStates> state = botUserService.findBotStateByChatId(chatId);

        if(state.isPresent() && state.get().equals(BotStates.EDITING_PROFILE_PHONE)){
            profileEnterPhoneNumberCommand.savePhoneNumber(chatId, contact.getPhoneNumber());
        }
    }
}
