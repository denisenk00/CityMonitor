package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
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
public class ProfileEnterNameCommand implements Command<Long> {

    private final BotUserService botUserService;
    private final TelegramService telegramService;
    private final CacheManager cacheManager;

    private static final String NOT_ACTIVE_USER_MESSAGE = "Ваше ім''я - {0}? Якщо ні, змініть відправивши нове";
    private static final String NOT_REGISTERED_USER_MESSAGE = "Як до вас звертатися? Напишіть своє ім'я";
    private static final String INCORRECT_NAME_MESSAGE = "Дані не схожі на ім'я, будь-ласка, перевірте та повторіть спробу";

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Zа-яА-я]");

    @Override
    public void execute(Long chatId) {
        log.info("Entering name command started: chatId = " + chatId);
        cacheManager.updateBotStateByChatId(chatId, BotStates.EDITING_PROFILE_NAME);
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
                log.info("User with chatId = " + chatId + " has already registered");
                String oldName = notActiveUser.getName();
                ReplyKeyboardMarkup keyboardMarkup = createNextStepKeyboard();
                telegramService.sendMessage(chatId, MessageFormat.format(NOT_ACTIVE_USER_MESSAGE, oldName), keyboardMarkup);
            },
            ()->{
                log.info("User with chatId = " + chatId + " isn't registered - new User");
                telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, null);
            });
        cacheManager.saveBotUserByChatId(chatId, botUser.orElse(new BotUser(chatId)));
    }

    private ReplyKeyboardMarkup createNextStepKeyboard(){
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.NEXT_STEP_COMMAND.getTitle()).build()
        )));
        return keyboardBuilder.build();
    }

    public void saveUserName(Long chatId, String name){
        log.info("Saving user name: chatId = " + chatId + ", name = " + name);
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find() || name.length() > 75) {
            if(botUserService.userIsRegistered(chatId)){
                telegramService.sendMessage(chatId, INCORRECT_NAME_MESSAGE, createNextStepKeyboard());
            }else {
                telegramService.sendMessage(chatId, INCORRECT_NAME_MESSAGE, null);
            }
            return;
        }
        Optional<BotUser> botUser = cacheManager.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(existedBotUser -> {
            existedBotUser.setName(name);
            cacheManager.saveBotUserByChatId(chatId, existedBotUser);
        },
        () -> log.error("User with chatId = " + chatId + " was not found in cache repository"));
    }


}
