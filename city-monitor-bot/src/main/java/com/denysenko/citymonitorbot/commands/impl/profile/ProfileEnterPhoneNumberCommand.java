package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.entities.BotUser;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Component
public class ProfileEnterPhoneNumberCommand implements CommandSequence<Long> {
    @Autowired
    private ProfileEnterNameCommand enterNameCommand;
    @Autowired
    private ProfileEnterLocationCommand enterLocationCommand;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private BotUserService botUserService;
    private String NOT_ACTIVE_USER_MESSAGE = "Ваш телефон - {0}? Якщо ні, змініть ввівши новий або натиснувши кнопку";
    private String NOT_REGISTERED_USER_MESSAGE = "Введіть ваш телефон або натисніть кнопку";
    private static final Pattern PHONE_NUMBER_PATTERN =
            Pattern.compile("^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$");

    @Override
    public void execute(Long chatId) {
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
                    String oldPhone = notActiveUser.getPhone();
                    ReplyKeyboardMarkup keyboardMarkup = createKeyboard(true);
                    telegramService.sendMessage(chatId, MessageFormat.format(NOT_ACTIVE_USER_MESSAGE, oldPhone), keyboardMarkup);
                },
                ()->{
                    ReplyKeyboardMarkup keyboardMarkup = createKeyboard(false);
                    telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, keyboardMarkup);
                });
        botUserService.updateBotStateByChatId(chatId, BotStates.EDITING_PROFILE_PHONE);
    }

    public void savePhoneNumber(Long chatId, String phoneNumber){
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if (!matcher.find()) {
            telegramService.sendMessage(chatId,
                    "You entered the incorrect phone number, try again or press button.", null);
            return;
        }
        Optional<BotUser> botUser = botUserService.findBotUserInCacheByChatId(chatId);
        botUser.ifPresentOrElse(existedBotUser -> {
                    existedBotUser.setPhone(phoneNumber);
                    botUserService.updateBotUserInCacheByChatId(chatId, existedBotUser);
                },
                () -> {
                    //LOG!!!
                });
        executeNext(chatId);
    }

    private ReplyKeyboardMarkup createKeyboard(boolean skipStep) {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        if (skipStep) {
            keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                    builder().text(Commands.NEXT_STEP_COMMAND.getTitle()).build()
            )));
        }

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text("\uD83D\uDCF1 Send phone number").requestContact(true).build()
        )));

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.PREVIOUS_STEP_COMMAND.getTitle()).build()
        )));

        return keyboardBuilder.build();
    }

    @Override
    public void executePrevious(Long chatId){
        enterNameCommand.execute(chatId);
    }
    @Override
    public void executeNext(Long chatId){
        enterLocationCommand.execute(chatId);
    }
}
