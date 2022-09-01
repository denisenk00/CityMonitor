package com.denysenko.citymonitorbot.handlers;

import com.denysenko.citymonitorbot.CityMonitorBotApplication;
import com.denysenko.citymonitorbot.handlers.impl.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SuperUpdateHandler {
    private static final Logger LOG = Logger.getLogger(SuperUpdateHandler.class);
    @Autowired
    private CallBackHandler callBackHandler;
    @Autowired
    private ContactHandler contactHandler;
    @Autowired
    private LocationHandler locationHandler;
    @Autowired
    private ReplyButtonHandler replyButtonHandler;
    @Autowired
    private TextHandler textHandler;

    public Set<Handler> getHandlers(){
        Set<Handler> handlers = new LinkedHashSet<>();
        handlers.add(callBackHandler);
        handlers.add(contactHandler);
        handlers.add(locationHandler);
        handlers.add(replyButtonHandler);
        handlers.add(textHandler);
        return handlers;
    }

    public void handle(Update update) {
        getHandlers().stream()
                .filter(handler -> {
                    LOG.info(handler.getClass().getName() + "is Applicable " +  handler.isApplicable(update));
                    return handler.isApplicable(update);
                })
                .findFirst()
                .ifPresent(handler -> {
                    LOG.info("Execute " + handler.getClass().getName());
                    handler.handle(update);
                });
    }
}
