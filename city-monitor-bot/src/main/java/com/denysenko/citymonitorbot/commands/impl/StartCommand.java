package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command <Long>{
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ProfileEnterNameCommand profileEnterNameCommand;
    @Autowired
    private MainMenuCommand mainMenuCommand;

    private String BASIC_MESSAGE = "Привіт! Раді вітати тебе в нашому чат-боті. Ми налаштовані на тісну співпрацю з тобою, тож будь активним!\nРазом ми зможемо зробити наше місто кращим)";
    private String NOT_ACTIVE_USER_MESSAGE = "Хмм.. Я тебе пам'ятаю, перевір будь-ласка свої дані. Якщо хочеш оновити, просто відправ нам нові";
    private String NOT_REGISTERED_USER_MESSAGE = "Введіть особисті дані";

    @Override
    public void execute(Long chatId) {
        telegramService.sendMessage(chatId, BASIC_MESSAGE, null);
        botUserService.findBotUserByChatId(chatId).ifPresentOrElse(botUser -> {
            if(!botUser.isActive()){
                telegramService.sendMessage(chatId, NOT_ACTIVE_USER_MESSAGE, null);
                profileEnterNameCommand.execute(chatId);
                //we remember you..
                //execute next comm
            }else {
                executeMainMenuCommand(chatId);
                //main menu
            }
        },
                ()->{
            //register
                    telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, null);
                    profileEnterNameCommand.execute(chatId);
                });

    }

    public void executeMainMenuCommand(Long chatId){
        mainMenuCommand.execute(chatId);
    }
}
