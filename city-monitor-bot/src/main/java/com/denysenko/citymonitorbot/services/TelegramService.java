package com.denysenko.citymonitorbot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.validation.constraints.NotNull;

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

    public void sendMessage(@NotNull Long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(String.valueOf(chatId));
        if(text != null && !text.isEmpty()){
            messageBuilder.text(text);
        }
        if(replyKeyboardMarkup != null){
            messageBuilder.replyMarkup(replyKeyboardMarkup);
        }
        try {
            execute(messageBuilder.build());
        } catch (TelegramApiException e) {
            // throw new Exception("Failed send text message " + message, e);
        }
    }

}
