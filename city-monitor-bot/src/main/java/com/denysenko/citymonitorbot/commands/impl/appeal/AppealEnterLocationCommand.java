package com.denysenko.citymonitorbot.commands.impl.appeal;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@RequiredArgsConstructor
@Component
public class AppealEnterLocationCommand implements Command<Long> {

    private final TelegramService telegramService;
    private final CacheManager cacheManager;

    private static final String MESSAGE = "Тут ви можете прикріпити геолокацію";

    @Override
    public void execute(Long chatId) {
        log.info("Entering location for appeal started: chatId = " + chatId);
        cacheManager.updateBotStateByChatId(chatId, BotStates.APPEAL_ENTERING_LOCATION);
        ReplyKeyboardMarkup replyKeyboardMarkup = createKeyboard();
        telegramService.sendMessage(chatId, MESSAGE, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);
        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text("\uD83D\uDEA9 Поточне місцезнаходження").requestLocation(true).build()
        )));
        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.CANCEL_GENERAL_COMMAND.getTitle()).build(),
                builder().text(Commands.SAVE_APPEAL_COMMAND.getTitle()).build()
        )));

        return keyboardBuilder.build();
    }

    public void saveLocation(Long chatId, Double latitude, Double longitude){
        log.info("Saving location started: chatId = " + chatId + ", latitude = " + latitude + ", longitude = " + longitude);

        Optional<Appeal> appeal = cacheManager.findAppealByChatId(chatId);
        appeal.ifPresentOrElse(existedAppeal -> {
             Point locationPoint = new GeometryFactory().createPoint(new Coordinate(latitude, longitude));
             existedAppeal.setLocationPoint(locationPoint);
        },
        () -> log.error("Appeal for user with chatId = " + chatId + " was not found in repository"));
    }

}
