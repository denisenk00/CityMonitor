package com.denysenko.citymonitorweb.services.telegram;

import com.denysenko.citymonitorweb.models.entities.Quiz;

public interface TelegramService {
    void sendQuizByChatId(Long chatId, Quiz quiz);
}
