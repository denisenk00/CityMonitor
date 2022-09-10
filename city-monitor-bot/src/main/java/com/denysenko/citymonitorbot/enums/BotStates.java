package com.denysenko.citymonitorbot.enums;

public enum BotStates {
    MAIN_MENU("main"),
    EDITING_PROFILE_NAME("profile:entering_name"),
    EDITING_PROFILE_PHONE("profile:entering_phone"),
    EDITING_PROFILE_LOCATION("profile:entering_location"),
    PROFILE_MENU("profile"),
    APPEAL_ENTERING_DESCRIPTION("appeal:entering_description"),
    APPEAL_ATTACHING_FILES("appeal:attaching_files"),
    APPEAL_ENTERING_LOCATION("appeal:entering_location");

    private String title;

    BotStates(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
