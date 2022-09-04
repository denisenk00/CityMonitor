package com.denysenko.citymonitorbot.services;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class TelegramService extends DefaultAbsSender {

    private static final Logger LOG = Logger.getLogger(TelegramService.class);
    @Value("${telegram.bot.token}")
    private String botToken;

    protected TelegramService() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(String.valueOf(chatId));
        if(text == null){
            messageBuilder.text(Strings.EMPTY);
        }else {
            messageBuilder.text(text.trim());
        }
        if(replyKeyboardMarkup != null){
            messageBuilder.replyMarkup(replyKeyboardMarkup);
        }
        try {
            execute(messageBuilder.build());
        } catch (TelegramApiException e) {
            LOG.error("Failed sending message to chatId = " + chatId + "\n" + e);
            e.printStackTrace();
        }
    }

    public void sendLocation(Long chatId, Double latitude, Double longitude){
        SendLocation.SendLocationBuilder locationBuilder = SendLocation.builder();
        locationBuilder.latitude(latitude);
        locationBuilder.longitude(longitude);
        locationBuilder.chatId(chatId.toString());
        try {
            execute(locationBuilder.build());
        } catch (TelegramApiException e) {
            LOG.error("Failed sending location to chatId = " + chatId + "\n" + e);
            e.printStackTrace();
        }
    }

    public Optional<byte[]> downloadFileByFileID(String fileId){
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        byte[] fileInBytes = null;
        try {
            String filePath = execute(getFile).getFilePath();
            InputStream inputStream = downloadFileAsStream(filePath);
            fileInBytes = inputStream.readAllBytes();
        } catch (TelegramApiException | IOException e) {
            LOG.error("Failed downloadingFile: fileId = " + fileId + "\n" + e);
            e.printStackTrace();
        }
        return Optional.ofNullable(fileInBytes);
    }

}
