package com.denysenko.citymonitorbot.enums;

public enum Commands {
    START_COMMAND ("/start"),
    PROFILE_COMMAND("Profile"),
    EDIT_PROFILE_COMMAND("Edit"),
    STOP_BOT_COMMAND("Stop bot"),
    SEND_APPEAL_COMMAND("Send appeal"),
    CANCEL_GENERAL_COMMAND("Cancel"),
    NEXT_STEP_COMMAND("Proceed"),
    PREVIOUS_STEP_COMMAND("Back");

    private String title;

    Commands(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
