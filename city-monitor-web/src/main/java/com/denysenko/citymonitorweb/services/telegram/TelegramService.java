package com.denysenko.citymonitorweb.services.telegram;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;

@Validated
public interface TelegramService {
    void sendQuizToChats(@NotNull Iterable<Long> chatIDs, @NotNull Quiz quiz) throws TelegramApiException;
    FileInputStream getFileByID(@NotBlank String tgFileId) throws IOException, TelegramApiException;
}
