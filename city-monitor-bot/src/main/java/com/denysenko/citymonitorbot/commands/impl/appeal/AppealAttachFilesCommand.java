package com.denysenko.citymonitorbot.commands.impl.appeal;

import com.denysenko.citymonitorbot.commands.Command;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.models.File;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Optional;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

@Log4j
@RequiredArgsConstructor
@Component
public class AppealAttachFilesCommand implements Command<Long> {

    private final TelegramService telegramService;
    private final CacheManager cacheManager;

    private static final String MESSAGE = "Додайте фото або інші файли за необхідності і натисніть \"Далі\"";

    @Override
    public void execute(Long chatId) {
        log.info("Attaching files to appeal started: chatId = " + chatId);
        cacheManager.updateBotStateByChatId(chatId, BotStates.APPEAL_ATTACHING_FILES);
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

        Optional<Appeal> appeal = cacheManager.findAppealByChatId(chatId);
        appeal.ifPresentOrElse(existedAppeal -> {
             existedAppeal.addFile(file);
        },
        () -> log.error("Appeal for user with chatId = " + chatId + " was not found in cache repository"));
    }

}
