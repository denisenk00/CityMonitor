package com.denysenko.citymonitorbot.enums;

public enum Commands {
    START_COMMAND ("/start"),
    PROFILE_COMMAND("\uD83D\uDC64 Профіль"),
    EDIT_PROFILE_COMMAND("\uD83D\uDD27 Редагувати"),
    STOP_BOT_COMMAND("\uD83D\uDEAB Вимкнути"),
    SEND_APPEAL_COMMAND("\uD83D\uDCDD Написати звернення"),
    CANCEL_GENERAL_COMMAND("◀ Назад"),
    NEXT_STEP_COMMAND("Далі ▶"),
    PREVIOUS_STEP_COMMAND("◀ Попередній крок"),
    COMEBACK_COMMAND("\uD83D\uDD04 Повернутися");

    private String title;

    Commands(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
