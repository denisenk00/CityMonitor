package com.denysenko.citymonitorbot.commands.impl.profile;

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
public class ProfileMenuCommand implements Command<Long> {

    private final TelegramService telegramService;
    private final CacheManager cacheManager;

    @Override
    public void execute(Long chatId) {
        log.info("Profile menu command started: chatId = " + chatId);
        cacheManager.updateBotStateByChatId(chatId, BotStates.PROFILE_MENU);
        telegramService.sendMessage(chatId, "Управління профілем:", createProfileMenuKeyboard());
    }

    private ReplyKeyboardMarkup createProfileMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.EDIT_PROFILE_COMMAND.getTitle()).build(),
                builder().text(Commands.CANCEL_GENERAL_COMMAND.getTitle()).build()
        )));
        keyboardBuilder.keyboardRow(
                new KeyboardRow(Arrays.asList(builder().text(Commands.STOP_BOT_COMMAND.getTitle()).build()))
        );
        return keyboardBuilder.build();
    }
}
