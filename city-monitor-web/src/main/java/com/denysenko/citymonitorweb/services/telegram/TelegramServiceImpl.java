package com.denysenko.citymonitorweb.services.telegram;

import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
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
    public void sendQuizToLocals(Iterable<Long> chatIds, Quiz quiz) {
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

        try {
            for(Long chatId : chatIds){
                execute(messageBuilder.chatId(String.valueOf(chatId)).build());
                for(SendDocument document: sendDocumentList){
                    document.setChatId(String.valueOf(chatId));
                    execute(document);
                }
            }
        } catch (TelegramApiException e) {
            log.error(e);
        }
        log.info("Quiz id = " + quiz.getId() + " was successfully sent");
    }

    public java.io.File getFileByID(String tgFileId) throws IOException {

        URL url = new URL("https://api.telegram.org/bot" + botToken +"/getFile?file_id=" + tgFileId);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + botToken + "/" + file_path);
        java.io.File file = FileUtils.toFile(downoload);

        //FileOutputStream fos = new FileOutputStream(upPath + file_name);
//        System.out.println("Start upload");
//        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
//        //fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//        //fos.close();
//        rbc.close();
//        System.out.println("Uploaded!");
        return file;
    }

}