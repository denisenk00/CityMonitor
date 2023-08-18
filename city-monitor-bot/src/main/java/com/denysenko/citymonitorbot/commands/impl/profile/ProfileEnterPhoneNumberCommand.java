package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@RequiredArgsConstructor
@Component
public class ProfileEnterPhoneNumberCommand implements Command<Long> {

    private final TelegramService telegramService;
    private final BotUserService botUserService;

    private static final String NOT_ACTIVE_USER_MESSAGE = "Ваш телефон - {0}? Якщо хочете змінити, введіть новий або натисніть кнопку для відправки поточного";
    private static final String NOT_REGISTERED_USER_MESSAGE = "Введіть номер телефону або натисніть кнопку для відправки поточного, він буде використаний у разі необхідності зв'язку з вами";
    private static final String INCORRECT_PHONE_MESSAGE = "Ви ввели невірний номер телефона, спробуйте ще раз або натисніть кнопку.";
    private static final String PHONE_EXISTS_MESSAGE = "Користувач з цим номером телефона вже існує, введіть інший";

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$");

    @Override
    @Transactional
    public void execute(Long chatId) {
        log.info("Entering phone number command started: chatId = " + chatId);
        botUserService.updateBotStateByChatId(chatId, BotStates.EDITING_PROFILE_PHONE);
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
                log.info("User with chatId = " + chatId + " has already registered");
                String oldPhone = notActiveUser.getPhone();
                ReplyKeyboardMarkup keyboardMarkup = createKeyboard(true);
                telegramService.sendMessage(chatId, MessageFormat.format(NOT_ACTIVE_USER_MESSAGE, oldPhone), keyboardMarkup);
            },
            ()->{
                log.info("User with chatId = " + chatId + " isn't registered - new User");
                ReplyKeyboardMarkup keyboardMarkup = createKeyboard(false);
                telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, keyboardMarkup);
            });
    }

    @Transactional
    public void savePhoneNumber(Long chatId, String phoneNumber){
        log.info("Saving phone number: chatId = " + chatId + ", phoneNumber = " + phoneNumber);
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if (!matcher.find() || phoneNumber.length() > 13) {
            log.info("Incorrect phone number");
            ReplyKeyboardMarkup replyKeyboardMarkup = botUserService.userIsRegistered(chatId) ? createKeyboard(true) : createKeyboard(false);
            telegramService.sendMessage(chatId, INCORRECT_PHONE_MESSAGE, replyKeyboardMarkup);
            return;
        } else if(botUserService.existsUserByPhone(phoneNumber)){
            log.info("User with such phone already exists");
            ReplyKeyboardMarkup replyKeyboardMarkup = botUserService.userIsRegistered(chatId) ? createKeyboard(true) : createKeyboard(false);
            telegramService.sendMessage(chatId, PHONE_EXISTS_MESSAGE, replyKeyboardMarkup);
            return;
        }
        Optional<BotUser> botUser = botUserService.findBotUserInCacheByChatId(chatId);
        botUser.ifPresentOrElse(existedBotUser -> {
                existedBotUser.setPhone(phoneNumber);
                botUserService.updateBotUserInCacheByChatId(chatId, existedBotUser);
            },
            () -> log.error("User with chatId = " + chatId + " was not found in cache repository"));
    }

    private ReplyKeyboardMarkup createKeyboard(boolean skipStep) {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text("\uD83D\uDCF1 Поділитися номером телефону").requestContact(true).build()
        )));

        KeyboardRow nextPreviousRow = new KeyboardRow();
        nextPreviousRow.add(builder().text(Commands.PREVIOUS_STEP_COMMAND.getTitle()).build());
        if(skipStep){
            nextPreviousRow.add(builder().text(Commands.NEXT_STEP_COMMAND.getTitle()).build());
        }
        keyboardBuilder.keyboardRow(nextPreviousRow);
        return keyboardBuilder.build();
    }

}
