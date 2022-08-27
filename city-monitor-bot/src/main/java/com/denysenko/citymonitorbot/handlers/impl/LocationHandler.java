package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.handlers.Handler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class LocationHandler implements Handler {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().hasLocation();
    }

    @Override
    public void handle(Update update) {

    }
}
