package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChatMemberHandler implements Handler {

    private static final Logger LOG = Logger.getLogger(ChatMemberHandler.class);
    @Autowired
    private BotUserService botUserService;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMyChatMember();
    }

    @Override
    public void handle(Update update) {
        LOG.info("Update handled by ChatMemberHandler: updateID = " + update.getUpdateId());
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        String newStatus = chatMemberUpdated.getNewChatMember().getStatus();
        if(newStatus.equals("kicked")) {
            LOG.info("User kicked the bot from chat");
            Long chatId = chatMemberUpdated.getFrom().getId();
            botUserService.removeBotStateByChatId(chatId);
            botUserService.removeBotUserByChatIdFromCache(chatId);
            botUserService.deactivateBotUser(chatId);
        }
    }
}
