package com.denysenko.citymonitorweb.enums;

public enum UserAccountStatus {
    ACTIVE("Активний"),
    NOT_ACTIVE("Не активний");

    private String title;

    UserAccountStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
