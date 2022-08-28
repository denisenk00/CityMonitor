package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.entities.BotUser;
import com.denysenko.citymonitorbot.models.entities.LocationPoint;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Component
public class ProfileEnterLocationCommand implements CommandSequence<Long> {
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private MainMenuCommand mainMenuCommand;
    @Autowired
    private ProfileEnterPhoneNumberCommand enterPhoneNumberCommand;

    private String NOT_ACTIVE_USER_MESSAGE = "Ваше минуле місце проживання наведено зверху, для його зміни відправте нове або натисніть кнопку";
    private String NOT_REGISTERED_USER_MESSAGE = "Поділіться своїм місцем проживання";

    @Override
    public void execute(Long chatId) {
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
                    LocationPoint locationPoint = notActiveUser.getLocation();
                    ReplyKeyboardMarkup keyboardMarkup = createKeyboard(true);
                    telegramService.sendLocation(chatId, locationPoint.getLatitude().doubleValue(), locationPoint.getLongitude().doubleValue());
                    telegramService.sendMessage(chatId, NOT_ACTIVE_USER_MESSAGE, keyboardMarkup);
                },
                ()->{
                    ReplyKeyboardMarkup keyboardMarkup = createKeyboard(false);
                    telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, keyboardMarkup);
                });
        botUserService.updateBotStateByChatId(chatId, BotStates.EDITING_PROFILE_LOCATION);
    }

    public void saveLocation(Long chatId, Double latitude, Double longitude){
        LocationPoint locationPoint = new LocationPoint(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude));
        Optional<BotUser> botUser = botUserService.findBotUserInCacheByChatId(chatId);
        botUser.ifPresentOrElse(existedBotUser -> {
                    existedBotUser.setLocation(locationPoint);
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
                builder().text("Відправити локацію").requestLocation(true).build()
        )));

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.PREVIOUS_STEP_COMMAND.getTitle()).build()
        )));

        return keyboardBuilder.build();
    }

    public void executePrevious(Long chatId){
        enterPhoneNumberCommand.execute(chatId);
    }

    public void executeNext(Long chatId){
        mainMenuCommand.execute(chatId);
    }
}
