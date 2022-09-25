package com.denysenko.citymonitorweb.enums;

public enum AppealStatus {

    POSTED("Опубліковано"),
    VIEWED("Переглянуто"),
    PROCESSED("Опрацьовано");

    private String title;

    AppealStatus(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
