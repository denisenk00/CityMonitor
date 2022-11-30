package com.denysenko.citymonitorweb.services.telegram;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Service
public class TelegramServiceImpl extends DefaultAbsSender implements TelegramService {

    private static final Logger LOG = Logger.getLogger(TelegramServiceImpl.class);
    @Value("${telegram.bot.token}")
    private String botToken;

    protected TelegramServiceImpl() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void sendQuizByChatId(Long chatId, Quiz quiz) {

    }
}