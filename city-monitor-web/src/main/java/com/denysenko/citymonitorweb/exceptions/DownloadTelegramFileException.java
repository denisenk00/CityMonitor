package com.denysenko.citymonitorweb.exceptions;


public class DownloadTelegramFileException extends RuntimeException {
    String telegramFileId;

    public DownloadTelegramFileException(String telegramFileId) {
        this.telegramFileId = telegramFileId;
    }

    public DownloadTelegramFileException(String message, String telegramFileId) {
        super(message);
        this.telegramFileId = telegramFileId;
    }

    public DownloadTelegramFileException(String message, Throwable cause, String telegramFileId) {
        super(message, cause);
        this.telegramFileId = telegramFileId;
    }

    public DownloadTelegramFileException(Throwable cause, String telegramFileId) {
        super(cause);
        this.telegramFileId = telegramFileId;
    }
}
