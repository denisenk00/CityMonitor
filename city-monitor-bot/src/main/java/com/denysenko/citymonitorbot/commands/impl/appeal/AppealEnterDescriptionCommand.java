package com.denysenko.citymonitorbot.commands.impl.appeal;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.services.AppealService;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@RequiredArgsConstructor
@Component
public class AppealEnterDescriptionCommand implements Command<Long> {

    private final BotUserService botUserService;
    private final TelegramService telegramService;
    private final AppealService appealService;

    private static final String MESSAGE = "Почнемо з тексту.. Опишіть детально проблему або іншу мету цього звернення (до 2000 символів)";
    private static final String TEXT_SIZE_OVERFLOW = "Перевищено максимальну кількість символів. Для прикріплення великого обсягу інформації скористайтесь файлом";

    @Override
    @Transactional
    public void execute(Long chatId) {
        log.info("Entering description of appeal started: chatId = " + chatId);
        botUserService.updateBotStateByChatId(chatId, BotStates.APPEAL_ENTERING_DESCRIPTION);
        ReplyKeyboardMarkup replyKeyboardMarkup = createKeyboard();
        telegramService.sendMessage(chatId, MESSAGE, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);
        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.CANCEL_GENERAL_COMMAND.getTitle()).build()
        )));
        return keyboardBuilder.build();
    }

    @Transactional
    public void saveDescription(Long chatId, String text){
        log.info("Saving description started: chatId = " + chatId + "text = " + text);
        if(text.length() > 2000){
            telegramService.sendMessage(chatId, TEXT_SIZE_OVERFLOW, null);
            return;
        }
        createAppealInCache(chatId);

        Optional<Appeal> appeal = appealService.findAppealInCacheByChatId(chatId);
        appeal.ifPresentOrElse(existedAppeal -> {
             existedAppeal.setText(text);
             appealService.updateAppealInCacheByChatId(chatId, existedAppeal);
        },
        () -> log.error("Appeal for user with chatId = " + chatId + " was not found in cache repository"));
    }

    private void createAppealInCache(Long chatId){
        BotUser botUser = botUserService.findBotUserByChatId(chatId).get();
        Appeal appeal = new Appeal();
        appeal.setBotUserId(botUser.getBotUserId());
        appealService.updateAppealInCacheByChatId(chatId, appeal);
    }

}
