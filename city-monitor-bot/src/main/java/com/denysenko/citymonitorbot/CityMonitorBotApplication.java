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

    public static void main(String[] args){
        SpringApplication.run(CityMonitorBotApplication.class, args);
        LOG.info("RUN!!");
    }
}
