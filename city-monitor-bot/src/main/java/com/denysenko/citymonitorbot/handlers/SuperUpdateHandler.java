package com.denysenko.citymonitorbot.handlers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Component
public class SuperUpdateHandler {

    private static final Logger LOG = Logger.getLogger(SuperUpdateHandler.class);
    @Autowired
    private Set<Handler> handlers;

    public void handle(Update update) {
        LOG.info("Handled update: updateId = " + update.getUpdateId().toString());
        handlers.stream()
                .filter(handler -> handler.isApplicable(update))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }

}
