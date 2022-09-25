package com.denysenko.citymonitorweb.enums;

public enum QuizStatus {

    PLANNED("Заплановано"),
    IN_PROGRESS("В дії"),
    FINISHED("Завершено");

    private String title;

    QuizStatus (String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
