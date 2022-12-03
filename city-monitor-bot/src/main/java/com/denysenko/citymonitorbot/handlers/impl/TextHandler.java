package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterDescriptionCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j
@Component
public class TextHandler implements Handler {

    @Autowired
    private BotUserService botUserService;
    @Autowired
    private ProfileEnterNameCommand profileEnterNameCommand;
    @Autowired
    private ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    @Autowired
    private AppealEnterDescriptionCommand appealEnterDescriptionCommand;

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return false;
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);
        if(botUserState.isPresent()){
            BotStates botState = botUserState.get();
            return botState.equals(BotStates.EDITING_PROFILE_NAME) || botState.equals(BotStates.EDITING_PROFILE_PHONE)
                    || botState.equals(BotStates.APPEAL_ENTERING_DESCRIPTION);
        } else return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        log.info("Update handled by TextHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());
        BotStates botState = botUserService.findBotStateByChatId(chatId).get();

        if(botState.equals(BotStates.EDITING_PROFILE_NAME)){
            profileEnterNameCommand.saveUserName(chatId, message.getText());
        }else if(botState.equals(BotStates.EDITING_PROFILE_PHONE)){
            profileEnterPhoneNumberCommand.savePhoneNumber(chatId, message.getText());
        }else if(botState.equals(BotStates.APPEAL_ENTERING_DESCRIPTION)){
            appealEnterDescriptionCommand.saveDescription(chatId, message.getText());
        }
    }
}
