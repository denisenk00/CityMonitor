package com.denysenko.citymonitorweb.services.telegram;

import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Log4j
@Service
public class TelegramServiceImpl extends DefaultAbsSender implements TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    protected TelegramServiceImpl() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Transactional
    @Override
    public void sendQuizToChats(Iterable<Long> chatIds, Quiz quiz) throws TelegramApiException{
        log.info("Sending quiz: quiz = " + quiz.toString());
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder replyKeyboardBuilder = InlineKeyboardMarkup.builder();

        for(Option option : quiz.getOptions()){
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(option.getTitle());
            button.setCallbackData("answer:quiz_id=" + quiz.getId() + ",option_id=" + option.getId());
            replyKeyboardBuilder.keyboardRow(List.of(button));
        }

        messageBuilder.text(quiz.getDescription());
        messageBuilder.replyMarkup(replyKeyboardBuilder.build());
        List<SendDocument> sendDocumentList = new LinkedList<>();

        for(File file : quiz.getFiles()){
            InputFile inputFile = new InputFile();
            inputFile.setMedia(new ByteArrayInputStream(file.getContent()), file.getName());
            SendDocument sendDocument = SendDocument.builder()
                    .chatId(Strings.EMPTY)
                    .document(inputFile)
                    .build();
            sendDocumentList.add(sendDocument);
        }

        for (Long chatId : chatIds) {
            execute(messageBuilder.chatId(String.valueOf(chatId)).build());
            for (SendDocument document : sendDocumentList) {
                document.setChatId(String.valueOf(chatId));
                execute(document);
            }
        }

        log.info("Quiz id = " + quiz.getId() + " was successfully sent");
    }

    public FileInputStream getFileByID(String tgFileId) throws TelegramApiException, FileNotFoundException {
        log.info("getting file from telegram server by id = " + tgFileId);
        GetFile getFile = new GetFile(tgFileId);

        org.telegram.telegrambots.meta.api.objects.File tgfile = execute(getFile);
        String filePath = tgfile.getFilePath();
        java.io.File file = downloadFile(filePath);
        return new FileInputStream(file);
    }

}