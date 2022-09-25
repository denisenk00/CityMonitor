package com.denysenko.citymonitorweb.enums;

public enum LayoutStatus {

    IN_USE("Переглянуто"),
    AVAILABLE("Опрацьовано");

    private String title;

    LayoutStatus(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
