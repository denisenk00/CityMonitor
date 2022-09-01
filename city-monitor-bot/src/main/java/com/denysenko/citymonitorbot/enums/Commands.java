package com.denysenko.citymonitorbot.enums;

public enum Commands {
    START_COMMAND ("/start"),
    PROFILE_COMMAND("Профіль"),
    EDIT_PROFILE_COMMAND("Редагувати"),
    STOP_BOT_COMMAND("Вимкнути"),
    SEND_APPEAL_COMMAND("Написати звернення"),
    CANCEL_GENERAL_COMMAND("Назад"),
    NEXT_STEP_COMMAND("Далі"),
    PREVIOUS_STEP_COMMAND("Попередній крок"),
    COMEBACK_COMMAND("Повернутися");

    private String title;

    Commands(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
