package com.denysenko.citymonitorbot.enums;

public enum BotStates {
    MAIN_MENU("main"),
    EDITING_PROFILE_NAME("profile:entering_name"),
    EDITING_PROFILE_PHONE("profile:entering_phone"),
    EDITING_PROFILE_LOCATION("profile:entering_location"),
    PROFILE_MENU("profile"),
    SEND_MESSAGE_MENU("send_message");

    private String title;

    BotStates(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
