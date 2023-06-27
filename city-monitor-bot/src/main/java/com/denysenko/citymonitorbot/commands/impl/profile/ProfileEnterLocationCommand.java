package com.denysenko.citymonitorbot.commands.impl.profile;

import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@RequiredArgsConstructor
@Component
public class ProfileEnterLocationCommand implements CommandSequence<Long> {

    private final TelegramService telegramService;
    private final BotUserService botUserService;
    private final MainMenuCommand mainMenuCommand;
    @Autowired
    private ProfileEnterPhoneNumberCommand enterPhoneNumberCommand;

    private static final String NOT_ACTIVE_USER_MESSAGE = "Ваше поточне місце проживання наведено зверху, для його зміни відправте нове або натисніть кнопку";
    private static final String NOT_REGISTERED_USER_MESSAGE = "Поділіться своїм місцем проживання, натиснувши кнопку або відправивши вручну";

    @Override
    @Transactional
    public void execute(Long chatId) {
        log.info("Enter location command started: chatId = " + chatId);
        botUserService.updateBotStateByChatId(chatId, BotStates.EDITING_PROFILE_LOCATION);
        Optional<BotUser> botUser = botUserService.findBotUserByChatId(chatId);
        botUser.ifPresentOrElse(notActiveUser -> {
                log.info("User with chatId = " + chatId + " has already registered");
                Point locationPoint = notActiveUser.getLocation();
                ReplyKeyboardMarkup keyboardMarkup = createKeyboard(true);
                telegramService.sendLocation(chatId, locationPoint.getX(), locationPoint.getY());
                telegramService.sendMessage(chatId, NOT_ACTIVE_USER_MESSAGE, keyboardMarkup);
            },
            ()->{
                log.info("User with chatId = " + chatId + " isn't registered - new User");
                ReplyKeyboardMarkup keyboardMarkup = createKeyboard(false);
                telegramService.sendMessage(chatId, NOT_REGISTERED_USER_MESSAGE, keyboardMarkup);
            });
    }

    @Transactional
    public void saveLocation(Long chatId, Double latitude, Double longitude){
        log.info("Saving location started. Data: chatId = " + chatId + ", latitude = " + latitude + ", longitude = " + longitude);
        Optional<BotUser> botUser = botUserService.findBotUserInCacheByChatId(chatId);
        botUser.ifPresentOrElse(existedBotUser -> {
                Point newLocation = new GeometryFactory().createPoint(new Coordinate(latitude, longitude));
                existedBotUser.setLocation(newLocation);
                botUserService.updateBotUserInCacheByChatId(chatId, existedBotUser);
            },
            () ->  log.error("User with chatId = " + chatId + " was not found in cache repository"));
        executeNext(chatId);
    }


    private ReplyKeyboardMarkup createKeyboard(boolean skipStep) {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text("\uD83D\uDEA9 Відправити локацію").requestLocation(true).build()
        )));

        KeyboardRow nextPreviousRow = new KeyboardRow();
        nextPreviousRow.add(builder().text(Commands.PREVIOUS_STEP_COMMAND.getTitle()).build());
        if (skipStep) {
            nextPreviousRow.add(builder().text(Commands.NEXT_STEP_COMMAND.getTitle()).build());
        }
        keyboardBuilder.keyboardRow(nextPreviousRow);

        return keyboardBuilder.build();
    }
    @Override
    public void executePrevious(Long chatId){
        enterPhoneNumberCommand.execute(chatId);
    }
    @Override
    @Transactional
    public void executeNext(Long chatId){
        saveBotUserToDBAndRemoveCached(chatId);
        mainMenuCommand.execute(chatId);
    }

    private void saveBotUserToDBAndRemoveCached(Long chatId){
        log.info("Saving user data to remote database and clearing caches: chatId = " + chatId);
        Optional<BotUser> cachedUser = botUserService.findBotUserInCacheByChatId(chatId);
        cachedUser.get().setActive(true);
        botUserService.saveBotUserToDB(cachedUser.get());
        botUserService.removeBotUserByChatIdFromCache(chatId);
    }
}
