package com.denysenko.citymonitorbot.commands;

import com.denysenko.citymonitorbot.enums.Commands;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

public class CommandUtils {
    public static ReplyKeyboardMarkup createMainMenuKeyboard(){
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.PROFILE_COMMAND.getTitle()).build(),
                builder().text(Commands.SEND_MESSAGE_COMMAND.getTitle()).build()
        )));
        return keyboardBuilder.build();
    }


}
