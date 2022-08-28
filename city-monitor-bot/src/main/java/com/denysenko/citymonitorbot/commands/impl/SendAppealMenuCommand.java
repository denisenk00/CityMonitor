package com.denysenko.citymonitorbot.commands.impl;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.entities.Appeal;
import com.denysenko.citymonitorbot.models.entities.LocationPoint;
import com.denysenko.citymonitorbot.services.AppealService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Component
public class SendAppealMenuCommand implements Command<Long> {
    @Autowired
    private MainMenuCommand mainMenuCommand;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private AppealService appealService;

    private String MESSAGE = "Тут ви можете написати звернення прикріпивши будь-які матеріали";

    @Override
    public void execute(Long chatId) {
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
        Appeal appeal = new Appeal();
        appeal.setChatId(chatId);
        appeal.setStatus("POSTED");
        appeal.setPostDate(LocalDateTime.now());
        if(message.hasText()){
            appeal.setText(message.getText().trim());
        }
        if(message.hasLocation()){
            Location sentLocation = message.getLocation();
            BigDecimal latitude = BigDecimal.valueOf(sentLocation.getLatitude());
            BigDecimal longitude = BigDecimal.valueOf(sentLocation.getLongitude());
            appeal.setLocationPoint(new LocationPoint(latitude, longitude));
        }
        if(message.hasPhoto()){
            List<PhotoSize> photos = message.getPhoto();
            String fId = photos.get(photos.size()-1).getFileId();
            Optional<byte[]> image = telegramService.downloadFileByFileID(fId);
            image.ifPresent(im -> appeal.setImage(im));
        }

        appealService.saveAppeal(appeal);
        executePrevious(chatId);
    }

    public void executePrevious(Long chatId){
        mainMenuCommand.execute(chatId);
    }

}
