package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StartCommand implements Command <Long>{

    private static final Logger LOG = Logger.getLogger(StartCommand.class);
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ProfileEnterNameCommand profileEnterNameCommand;
    @Autowired
    private MainMenuCommand mainMenuCommand;

    private static final String BASIC_MESSAGE = "Привіт! Раді вітати тебе в нашому чат-боті. Ми налаштовані на тісну співпрацю з тобою, тож будь активним!\nРазом ми зможемо зробити наше місто кращим)";
    private static final String NOT_ACTIVE_USER_MESSAGE = "Хмм.. Я тебе пам'ятаю, перевір будь-ласка свої дані.";
    private static final String NOT_REGISTERED_USER_MESSAGE = "Введіть свої особисті дані для реєстрації";

    @Override
    public void execute(Long chatId) {
        LOG.info("Executing start command: chatId = " + chatId);
        telegramService.sendMessage(chatId, BASIC_MESSAGE, null);
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(user -> {
                LOG.info("User is registered");
                if(!user.isActive()){
                    LOG.info("User isn't active");
                    telegramService.sendMessage(chatId, NOT_ACTIVE_USER_MESSAGE, null);
                    profileEnterNameCommand.execute(chatId);
                }else {
                    LOG.info("User is active");
                    executeMainMenuCommand(chatId);
                }
        },
        ()->{
                LOG.info("User is not registered");
                telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, null);
                profileEnterNameCommand.execute(chatId);
        });

    }

    public void executeMainMenuCommand(Long chatId){
        mainMenuCommand.execute(chatId);
    }
}
