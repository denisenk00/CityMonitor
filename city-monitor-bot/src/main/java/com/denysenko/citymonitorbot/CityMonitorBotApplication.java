package com.denysenko.citymonitorbot;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityMonitorBotApplication {

    private static final Logger LOG = Logger.getLogger(CityMonitorBotApplication.class);

    public static void main(String[] args){
        LOG.info("Application starting..");
        SpringApplication.run(CityMonitorBotApplication.class, args);
    }
}
