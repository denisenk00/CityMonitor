package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.handlers.Handler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallBackHandler implements Handler {
    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public void handle(Update update) {

    }
}
