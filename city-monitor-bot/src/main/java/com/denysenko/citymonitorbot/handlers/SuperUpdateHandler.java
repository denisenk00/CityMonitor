package com.denysenko.citymonitorbot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Log4j
@RequiredArgsConstructor
@Component
public class SuperUpdateHandler {

    private final Set<Handler> handlers;

    public void handle(Update update) {
        log.info("Handled update: updateId = " + update.getUpdateId().toString());
        handlers.stream()
                .filter(handler -> handler.isApplicable(update))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }

}
