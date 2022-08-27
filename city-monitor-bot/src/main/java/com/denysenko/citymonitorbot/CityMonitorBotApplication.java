package com.denysenko.citymonitorbot;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class CityMonitorBotApplication {
    private static final Logger LOG = Logger.getLogger(CityMonitorBotApplication.class);
    private static TelegramBot telegramBot;

    @Autowired
    public CityMonitorBotApplication(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public static void main(String[] args){
        SpringApplication.run(CityMonitorBotApplication.class, args);
        try {
            LOG.info("Registering bot..");
            new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBot);
        } catch (TelegramApiException e) {
            LOG.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
        }
        LOG.info("Telegram Bot is ready to receive updates from users..");
    }

}
