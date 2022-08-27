package com.denysenko.citymonitorbot.handlers;

import com.denysenko.citymonitorbot.CityMonitorBotApplication;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SuperUpdateHandler {
    private static final Logger LOG = Logger.getLogger(SuperUpdateHandler.class);

    private Set<Handler> handlers;

    @Autowired
    public void setHandlers(Handler ... handlers){
        this.handlers = Arrays.stream(handlers).collect(Collectors.toSet());
        LOG.info("handlers size = " + this.handlers.size());
    }

    public void handle(Update update) {
        handlers.stream()
                .filter(handler -> handler.isApplicable(update))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }
}
