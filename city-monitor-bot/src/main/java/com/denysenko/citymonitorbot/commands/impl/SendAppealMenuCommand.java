package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.models.LocationPoint;
import com.denysenko.citymonitorbot.services.AppealService;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Component
public class SendAppealMenuCommand implements Command<Long> {

    private static final Logger LOG = Logger.getLogger(SendAppealMenuCommand.class);
    @Autowired
    private MainMenuCommand mainMenuCommand;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private AppealService appealService;
    @Autowired
    private BotUserService botUserService;

    private static final String MESSAGE = "Тут ви можете написати звернення, прикріпивши до нього фото, зазначивши локацію";

    @Override
    public void execute(Long chatId) {
        LOG.info("Send appeal menu started: chatId = " + chatId);
        botUserService.updateBotStateByChatId(chatId, BotStates.SEND_APPEAL_MENU);
        ReplyKeyboardMarkup keyboardMarkup = createKeyboard();
        telegramService.sendMessage(chatId, MESSAGE, keyboardMarkup);
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);
        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.CANCEL_GENERAL_COMMAND.getTitle()).build()
        )));

        return keyboardBuilder.build();
    }

    public void saveAppeal(Long chatId, Message message){
        LOG.info("Saving appeal: chatId = " + chatId + ", message = " + message.toString());
        BotUser botUser = botUserService.findBotUserByChatId(chatId).get();
        Appeal appeal = new Appeal();
        appeal.setBotUserId(botUser.getBotUserId());
        appeal.setStatus("POSTED");
        appeal.setPostDate(LocalDateTime.now());
        boolean appealIsEmpty = true;
        if(message.hasText()){
            LOG.info("message has text");
            appeal.setText(message.getText().trim());
            appealIsEmpty = false;
        }
        if(message.hasLocation()){
            LOG.info("message has location");
            Location sentLocation = message.getLocation();
            Double latitude = sentLocation.getLatitude();
            Double longitude = sentLocation.getLongitude();
            appeal.setLocationPoint(new LocationPoint(latitude, longitude));
            appealIsEmpty = false;
        }
        if(message.hasPhoto()){
            LOG.info("message has photo");
            List<PhotoSize> photos = message.getPhoto();
            String fId = photos.get(photos.size()-1).getFileId();
            Optional<byte[]> image = telegramService.downloadFileByFileID(fId);
            image.ifPresent(im -> {
                try {
                    appeal.setImage(new SerialBlob(im));
                } catch (SQLException e) {
                    LOG.error("Error while gathering image " + e);
                }
            });
            appealIsEmpty = false;
        }

        if(!appealIsEmpty) {
            appealService.saveAppeal(appeal);
            executeMainMenuCommand(chatId);
        }
    }

    public void executeMainMenuCommand(Long chatId){
        mainMenuCommand.execute(chatId);
    }

}
