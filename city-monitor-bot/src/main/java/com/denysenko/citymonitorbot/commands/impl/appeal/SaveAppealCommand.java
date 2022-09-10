package com.denysenko.citymonitorbot.commands.impl.appeal;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.services.AppealService;
import com.denysenko.citymonitorbot.services.TelegramService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaveAppealCommand implements Command<Long> {

    private static final Logger LOG = Logger.getLogger(SaveAppealCommand.class);
    @Autowired
    private AppealService appealService;
    @Autowired
    private TelegramService telegramService;

    private static final String SUCCESS_SENDING_APPEAL_MESSAGE = "Ваше звернення збережено та буде оброблено найближчим часом. Наш працівник обов'язково зв'яжеться з вами якщо буде потреба";

    @Override
    public void execute(Long chatId) {
        LOG.info("Saving appeal data to remote database and clearing caches: chatId = " + chatId);
        Optional<Appeal> cachedAppeal = appealService.findAppealInCacheByChatId(chatId);
        cachedAppeal.ifPresentOrElse(existedAppeal -> {
             appealService.saveAppeal(existedAppeal);
             appealService.removeAppealByChatIdFromCache(chatId);
             telegramService.sendMessage(chatId, SUCCESS_SENDING_APPEAL_MESSAGE, null);
        },
        ()->LOG.error("Appeal for user with chatId = " + chatId + " was not found in cache repository"));
    }
}
