package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopCommand implements Command<Long> {
    @Autowired
    private BotUserService botUserService;

    @Override
    public void execute(Long chatId) {
        botUserService.deactivateBotUser(chatId);
        botUserService.removeBotStateByChatId(chatId);
    }
}
