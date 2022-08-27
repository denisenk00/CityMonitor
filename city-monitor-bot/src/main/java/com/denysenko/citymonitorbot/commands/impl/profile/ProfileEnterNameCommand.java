package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.Command;
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
public class ProfileEnterNameCommand implements Command<Long> {
    private BotUserService botUserService;
    private TelegramService telegramService;
    private ProfileEnterPhoneNumberCommand enterPhoneNumberCommand;


    private String NOT_ACTIVE_USER_MESSAGE = "Ваше ім'я - {0}? Якщо ні, змініть відправивши нове";
    private String NOT_REGISTERED_USER_MESSAGE = "Введіть ваше ім'я";
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Zа-яА-я]");

    @Autowired
    public ProfileEnterNameCommand(BotUserService botUserService, TelegramService telegramService, ProfileEnterPhoneNumberCommand enterPhoneNumberCommand) {
        this.botUserService = botUserService;
        this.telegramService = telegramService;
        this.enterPhoneNumberCommand = enterPhoneNumberCommand;
    }

    @Override
    public void execute(Long chatId) {
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
            String oldName = notActiveUser.getName();
            ReplyKeyboardMarkup keyboardMarkup = createNextStepKeyboard();
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

    public void executeNext(Long chatId) {
        enterPhoneNumberCommand.execute(chatId);
    }
}
