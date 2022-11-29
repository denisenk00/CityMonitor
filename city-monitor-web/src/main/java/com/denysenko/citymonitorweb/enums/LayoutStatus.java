package com.denysenko.citymonitorweb.enums;

import java.util.NoSuchElementException;

public enum LayoutStatus {

    IN_USE("Використовується"),
    AVAILABLE("Доступний для використання"),
    DEPRECATED("Застарілий");

    private String title;

    LayoutStatus(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static LayoutStatus getByTitle(String title) throws NoSuchElementException {
        for (LayoutStatus status : values()){
            if(status.getTitle().equals(title)) return status;
        }
        throw new IllegalArgumentException("Статусу - " + title + " не існує");
    }

}
