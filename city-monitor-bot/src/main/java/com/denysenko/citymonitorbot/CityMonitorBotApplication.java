package com.denysenko.citymonitorbot;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j
@SpringBootApplication
public class CityMonitorBotApplication {

    public static void main(String[] args){
        log.info("Application starting..");
        SpringApplication.run(CityMonitorBotApplication.class, args);
    }
}
