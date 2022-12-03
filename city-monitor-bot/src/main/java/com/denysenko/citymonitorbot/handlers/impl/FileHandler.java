package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.appeal.AppealAttachFilesCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.List;
import java.util.Optional;

@Log4j
@Component
public class FileHandler implements Handler {

    @Autowired
    private BotUserService botUserService;
    @Autowired
    private AppealAttachFilesCommand appealAttachFilesCommand;

    @Override
    public boolean isApplicable(Update update) {
        Message message = null;
        if(update.hasMessage()){
            message = update.getMessage();
            if(!(message.hasPhoto() || message.hasDocument() || message.hasVideo() || message.hasAudio()))
                return false;
        }
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);
        if(botUserState.isPresent()){
            BotStates botState = botUserState.get();
            return botState.equals(BotStates.APPEAL_ATTACHING_FILES);
        } else return false;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        log.info("Handled by FileHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());
        String name = null;
        String fileID = null;
        if(message.hasPhoto()){
            List<PhotoSize> photos = message.getPhoto();
            PhotoSize photoSize = photos.get(photos.size()-1);
            name = photoSize.getFilePath();
            fileID = photoSize.getFileId();
        } else if(message.hasDocument()){
            Document document = message.getDocument();
            name = document.getFileName();
            fileID = document.getFileId();
        } else if(message.hasVideo()){
            Video video = message.getVideo();
            name = video.getFileName();
            fileID = video.getFileId();
        } else if(message.hasAudio()){
            Audio audio = message.getAudio();
            name = audio.getFileName();
            fileID = audio.getFileId();
        }

        appealAttachFilesCommand.saveFile(chatId, name, fileID);
    }
}
