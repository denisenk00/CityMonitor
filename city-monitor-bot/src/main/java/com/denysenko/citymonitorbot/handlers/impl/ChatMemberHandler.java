package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
public class ChatMemberHandler implements Handler {

    @Autowired
    private BotUserService botUserService;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMyChatMember();
    }

    @Override
    public void handle(Update update) {
        log.info("Update handled by ChatMemberHandler: updateID = " + update.getUpdateId());
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        String newStatus = chatMemberUpdated.getNewChatMember().getStatus();
        if(newStatus.equals("kicked")) {
            log.info("User kicked the bot from chat");
            Long chatId = chatMemberUpdated.getFrom().getId();
            botUserService.removeBotStateByChatId(chatId);
            botUserService.removeBotUserByChatIdFromCache(chatId);
            botUserService.deactivateBotUser(chatId);
        }
    }
}
