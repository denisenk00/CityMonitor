package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@RequiredArgsConstructor
@Component
public class MainMenuCommand implements Command<Long> {

    private final TelegramService telegramService;
    private final CacheManager cacheManager;

    @Override
    public void execute(Long chatId) {
        log.info("Main menu command started: chatId = " + chatId);
        cacheManager.updateBotStateByChatId(chatId, BotStates.MAIN_MENU);
        telegramService.sendMessage(chatId, "Головне меню:", createMainMenuKeyboard());
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
