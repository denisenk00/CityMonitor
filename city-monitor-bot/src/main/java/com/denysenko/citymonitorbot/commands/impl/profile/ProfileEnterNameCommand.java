package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.entities.BotUser;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.apache.log4j.Logger;
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
public class ProfileEnterNameCommand implements CommandSequence<Long> {
    private static final Logger LOG = Logger.getLogger(ProfileEnterNameCommand.class);
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ProfileEnterPhoneNumberCommand enterPhoneNumberCommand;


    private String NOT_ACTIVE_USER_MESSAGE = "Ваше ім''я - {0}? Якщо ні, змініть відправивши нове";
    private String NOT_REGISTERED_USER_MESSAGE = "Як до вас звертатися? Напишіть своє ім'я";
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Zа-яА-я]");

    @Override
    public void execute(Long chatId) {
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
            String oldName = notActiveUser.getName();
            ReplyKeyboardMarkup keyboardMarkup = createNextStepKeyboard();
            LOG.info("Old name = " + oldName);
            telegramService.sendMessage(chatId, MessageFormat.format(NOT_ACTIVE_USER_MESSAGE, oldName), keyboardMarkup);
        },
        ()->{
            telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, null);
        });
        botUserService.updateBotUserInCacheByChatId(chatId, botUser.orElse(new BotUser(chatId)));
        botUserService.updateBotStateByChatId(chatId, BotStates.EDITING_PROFILE_NAME);
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
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            telegramService.sendMessage(chatId, "You entered the incorrect name, try again.", null);
            return;
        }
        Optional<BotUser> botUser = botUserService.findBotUserInCacheByChatId(chatId);
        botUser.ifPresentOrElse(existedBotUser -> {
            existedBotUser.setName(name);
            botUserService.updateBotUserInCacheByChatId(chatId, existedBotUser);
        },
        () -> {
            //LOG!!!
        });
        executeNext(chatId);
    }

    @Override
    public void executeNext(Long chatId) {
        enterPhoneNumberCommand.execute(chatId);
    }

    @Override
    public void executePrevious(Long aLong) {
    }

}
