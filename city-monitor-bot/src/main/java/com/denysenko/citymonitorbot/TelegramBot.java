package com.denysenko.citymonitorbot;

import com.denysenko.citymonitorbot.handlers.SuperUpdateHandler;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Autowired
    private SuperUpdateHandler superUpdateHandler;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
    @PostConstruct
    private void startBot(){
        try {
            log.info("Registering bot..");
            new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
            log.info("Telegram Bot is ready to receive updates from users..");
        } catch (TelegramApiException e) {
            log.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received: updateId = " + update.getUpdateId());
        superUpdateHandler.handle(update);
    }
}
