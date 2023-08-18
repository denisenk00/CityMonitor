package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Component
public class StartCommand implements Command <Long>{

    private final BotUserService botUserService;
    private final TelegramService telegramService;
    private final ProfileEnterNameCommand profileEnterNameCommand;
    private final MainMenuCommand mainMenuCommand;

    private static final String BASIC_MESSAGE = "Привіт! Раді вітати тебе в нашому чат-боті. Ми налаштовані на тісну співпрацю з тобою, тож будь активним!\nРазом ми зможемо зробити наше місто кращим)";
    private static final String NOT_ACTIVE_USER_MESSAGE = "Хмм.. Я тебе пам'ятаю, перевір будь-ласка свої дані.";
    private static final String NOT_REGISTERED_USER_MESSAGE = "Введіть свої особисті дані для реєстрації";

    @Override
    @Transactional
    public void execute(Long chatId) {
        log.info("Executing start command: chatId = " + chatId);
        telegramService.sendMessage(chatId, BASIC_MESSAGE, null);
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(user -> {
                log.info("User is registered");
                if(!user.isActive()){
                    log.info("User isn't active");
                    telegramService.sendMessage(chatId, NOT_ACTIVE_USER_MESSAGE, null);
                    profileEnterNameCommand.execute(chatId);
                }else {
                    log.info("User is active");
                    mainMenuCommand.execute(chatId);
                }
        },
        ()->{
                log.info("User is not registered");
                telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, null);
                profileEnterNameCommand.execute(chatId);
        });

    }

}
