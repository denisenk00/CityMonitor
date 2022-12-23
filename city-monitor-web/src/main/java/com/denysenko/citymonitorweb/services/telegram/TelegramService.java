package com.denysenko.citymonitorweb.services.telegram;

import com.denysenko.citymonitorweb.models.entities.Quiz;

import java.io.IOException;

public interface TelegramService {
    void sendQuizToLocals(Iterable<Long> chatIDs, Quiz quiz);
    java.io.File getFileByID(String tgFileId) throws IOException;
}
