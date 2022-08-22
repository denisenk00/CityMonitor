package com.denysenko.citymonitorbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CityMonitorBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityMonitorBotApplication.class, args);
    }

}
