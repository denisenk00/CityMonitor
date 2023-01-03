package com.denysenko.citymonitorbot.commands.impl.appeal;

import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.models.File;
import com.denysenko.citymonitorbot.services.AppealService;
import com.denysenko.citymonitorbot.services.BotUserService;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@Component
public class AppealAttachFilesCommand implements CommandSequence<Long> {

    @Autowired
    private AppealEnterLocationCommand appealEnterLocationCommand;
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private AppealService appealService;

    private static final String MESSAGE = "Додайте фото або інші файли за необхідності і натисніть \"Далі\"";

    @Override
    @Transactional
    public void execute(Long chatId) {
        log.info("Attaching files to appeal started: chatId = " + chatId);
        botUserService.updateBotStateByChatId(chatId, BotStates.APPEAL_ATTACHING_FILES);
        ReplyKeyboardMarkup replyKeyboardMarkup = createKeyboard();
        telegramService.sendMessage(chatId, MESSAGE, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);
        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.CANCEL_GENERAL_COMMAND.getTitle()).build(),
                builder().text(Commands.NEXT_STEP_COMMAND.getTitle()).build()
        )));

        return keyboardBuilder.build();
    }

    public void saveFile(Long chatId, String name, String fileId){
        log.info("Saving file started: chatId = " + chatId + ", name = " + name + ", fileId + " + fileId);
        File file = new File(name, fileId);

        Optional<Appeal> appeal = appealService.findAppealInCacheByChatId(chatId);
        appeal.ifPresentOrElse(existedAppeal -> {
             existedAppeal.getFiles().add(file);
             appealService.updateAppealInCacheByChatId(chatId, existedAppeal);
        },
        () -> log.error("Appeal for user with chatId = " + chatId + " was not found in cache repository"));
    }

    @Override
    public void executeNext(Long chatId) {
        appealEnterLocationCommand.execute(chatId);
    }

    @Override
    public void executePrevious(Long chatId) {}
}
