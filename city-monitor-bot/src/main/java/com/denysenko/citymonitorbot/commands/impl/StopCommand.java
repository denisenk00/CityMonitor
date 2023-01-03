package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@Component
public class StopCommand implements Command<Long> {

    @Autowired
    private BotUserService botUserService;
    @Autowired
    private TelegramService telegramService;

    private static final String STOP_MESSAGE = "Ваш профіль деактивовано. Ви більше не будете отримувати опитування і тим самим впливати на прийняття важливих для вас рішень. Якщо надумаєте - ми на вас завжди чекаємо!";

    @Override
    public void execute(Long chatId) {
        log.info("Executing stop command: chatID = " + chatId);
        botUserService.deactivateBotUser(chatId);
        botUserService.removeBotStateByChatId(chatId);
        telegramService.sendMessage(chatId, STOP_MESSAGE, createStoppedBotKeyboard());
    }

    private ReplyKeyboardMarkup createStoppedBotKeyboard(){
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);
        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(builder().text(Commands.COMEBACK_COMMAND.getTitle()).build())));
        return keyboardBuilder.build();
    }

}
