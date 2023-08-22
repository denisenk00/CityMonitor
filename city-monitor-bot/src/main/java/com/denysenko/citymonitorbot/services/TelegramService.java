package com.denysenko.citymonitorbot.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface TelegramService {

    String getBotToken();

    void sendMessage(Long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup);

    void sendLocation(Long chatId, Double latitude, Double longitude);

    void sendAnswerCallbackQuery(String callbackQueryId, String alertMessage);

}
