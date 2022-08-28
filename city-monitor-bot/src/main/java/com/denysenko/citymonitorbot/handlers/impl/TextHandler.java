package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.SendAppealMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class TextHandler implements Handler {
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private ProfileEnterNameCommand profileEnterNameCommand;
    @Autowired
    private ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    @Autowired
    private SendAppealMenuCommand sendAppealMenuCommand;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        Optional<BotStates> botState = botUserService.findBotStateByChatId(chatId);
        botState.ifPresent(bs -> {
            if(bs.equals(BotStates.EDITING_PROFILE_NAME)){
                profileEnterNameCommand.saveUserName(chatId, message.getText());
            }else if(bs.equals(BotStates.EDITING_PROFILE_PHONE)){
                profileEnterPhoneNumberCommand.savePhoneNumber(chatId, message.getText());
            }else if(bs.equals(BotStates.SEND_APPEAL_MENU)){
                sendAppealMenuCommand.saveAppeal(chatId, message);
            }
        });
    }
}
