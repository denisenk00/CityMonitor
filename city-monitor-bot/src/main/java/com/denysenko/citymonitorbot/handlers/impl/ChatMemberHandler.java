package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
@Component
public class ChatMemberHandler implements Handler {

    private final BotUserService botUserService;
    private final CacheManager cacheManager;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMyChatMember();
    }

    @Override
    public void handle(Update update) {
        log.info("Update handled by ChatMemberHandler: updateID = " + update.getUpdateId());
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        String newStatus = chatMemberUpdated.getNewChatMember().getStatus();
        if (newStatus.equals("kicked")) {
            log.info("User kicked the bot from chat");
            Long chatId = chatMemberUpdated.getFrom().getId();
            cacheManager.removeBotStateByChatId(chatId);
            cacheManager.removeBotUserByChatId(chatId);
            botUserService.deactivateBotUser(chatId);
        }
    }
}
