package com.denysenko.citymonitorweb.enums;

public enum AppealStatus {

    UNREAD("Непрочитано"),
    VIEWED("Переглянуто"),
    IN_PROGRESS("В роботі"),
    PROCESSED("Опрацьовано"),
    TRASH("Сміття");

    private String title;

    AppealStatus(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
