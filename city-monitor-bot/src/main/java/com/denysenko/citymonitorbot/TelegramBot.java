package com.denysenko.citymonitorbot;

import com.denysenko.citymonitorbot.handlers.SuperUpdateHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger LOG = Logger.getLogger(TelegramBot.class);
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
            LOG.info("Registering bot..");
            new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
            LOG.info("Telegram Bot is ready to receive updates from users..");
        } catch (TelegramApiException e) {
            LOG.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.info("Update received: updateId = " + update.getUpdateId());
        superUpdateHandler.handle(update);
    }
}
