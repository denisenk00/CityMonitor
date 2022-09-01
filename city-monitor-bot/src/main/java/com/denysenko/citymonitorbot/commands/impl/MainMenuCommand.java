package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Component
public class MainMenuCommand implements Command<Long> {
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private TelegramService telegramService;

    private static final Logger LOG = Logger.getLogger(MainMenuCommand.class);

    @Override
    public void execute(Long chatId) {
        LOG.info("Main Execute");
        botUserService.updateBotStateByChatId(chatId, BotStates.MAIN_MENU);
        telegramService.sendMessage(chatId, "Головне меню:", createMainMenuKeyboard());
        LOG.info("Main execute end");
    }

    private ReplyKeyboardMarkup createMainMenuKeyboard(){
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.PROFILE_COMMAND.getTitle()).build(),
                builder().text(Commands.SEND_APPEAL_COMMAND.getTitle()).build()
        )));
        return keyboardBuilder.build();
    }
}
