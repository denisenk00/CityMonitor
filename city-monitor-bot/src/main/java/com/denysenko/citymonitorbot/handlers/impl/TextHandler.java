package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.appeal.AppealAttachFilesCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterDescriptionCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Component
public class TextHandler implements Handler {

    private final ProfileEnterNameCommand profileEnterNameCommand;
    private final ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    private final AppealEnterDescriptionCommand appealEnterDescriptionCommand;
    private final ProfileEnterLocationCommand profileEnterLocationCommand;
    private final AppealAttachFilesCommand appealAttachFilesCommand;

    private final CacheManager cacheManager;

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return false;
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = cacheManager.findBotStateByChatId(chatId);
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
        BotStates botState = cacheManager.findBotStateByChatId(chatId).get();

        if(botState.equals(BotStates.EDITING_PROFILE_NAME)){
            profileEnterNameCommand.saveUserName(chatId, message.getText());
            profileEnterPhoneNumberCommand.execute(chatId);
        }else if(botState.equals(BotStates.EDITING_PROFILE_PHONE)){
            profileEnterPhoneNumberCommand.savePhoneNumber(chatId, message.getText());
            profileEnterLocationCommand.execute(chatId);
        }else if(botState.equals(BotStates.APPEAL_ENTERING_DESCRIPTION)){
            appealEnterDescriptionCommand.saveDescription(chatId, message.getText());
            appealAttachFilesCommand.execute(chatId);
        }
    }
}
