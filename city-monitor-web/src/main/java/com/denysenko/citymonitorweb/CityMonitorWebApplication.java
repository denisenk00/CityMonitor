package com.denysenko.citymonitorweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class CityMonitorWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityMonitorWebApplication.class, args);
    }

}
