package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.exceptions.SendMessageException;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Service
public class TelegramService extends DefaultAbsSender {

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
            log.error("Failed sending message to chatId = " + chatId, e);
            e.printStackTrace();
            throw new SendMessageException("Failed sending message to chatId = " + chatId, e);
        }
    }

    public void sendLocation(Long chatId, Double latitude, Double longitude){
        SendLocation.SendLocationBuilder locationBuilder = SendLocation.builder();
        locationBuilder.latitude(longitude);
        locationBuilder.longitude(latitude);
        locationBuilder.chatId(chatId.toString());
        try {
            execute(locationBuilder.build());
        } catch (TelegramApiException e) {
            log.error("Failed sending location to chatId = " + chatId, e);
            e.printStackTrace();
            throw new SendMessageException("Failed sending location to chatId = " + chatId, e);
        }
    }

    public void sendAnswerCallbackQuery(String callbackQueryId, String alertMessage){
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        answerCallbackQuery.setText(alertMessage);
        answerCallbackQuery.setShowAlert(true);
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Failed sending answer to callback query id = " + callbackQueryId, e);
            throw new SendMessageException("Failed sending answer to callback query id = " + callbackQueryId, e);
        }
    }

}
