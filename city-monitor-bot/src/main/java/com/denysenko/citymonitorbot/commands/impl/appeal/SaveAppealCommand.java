package com.denysenko.citymonitorbot.commands.impl.appeal;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.entity.AppealService;
import com.denysenko.citymonitorbot.services.TelegramService;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Component
public class SaveAppealCommand implements Command<Long> {

    private final AppealService appealService;
    private final TelegramService telegramService;
    private final CacheManager cacheManager;
    private final BotUserService botUserService;

    private static final String SUCCESS_SENDING_APPEAL_MESSAGE = "Ваше звернення збережено та буде оброблено найближчим часом." +
            " Наш працівник обов'язково зв'яжеться з вами якщо буде потреба";

    @Override
    public void execute(Long chatId) {
        log.info("Saving appeal data to remote database and clearing caches: chatId = " + chatId);
        Optional<Appeal> cachedAppeal = cacheManager.findAppealByChatId(chatId);
        cachedAppeal.ifPresentOrElse(existedAppeal -> {
             Long botUserId = botUserService.getBotUserByChatId(chatId).getBotUserId();
             existedAppeal.setBotUserId(botUserId);
             appealService.saveAppeal(existedAppeal);
             cacheManager.removeAppealByChatId(chatId);
             telegramService.sendMessage(chatId, SUCCESS_SENDING_APPEAL_MESSAGE, null);
        },
        () -> log.error("Appeal for user with chatId = " + chatId + " was not found in cache repository"));
    }
}
