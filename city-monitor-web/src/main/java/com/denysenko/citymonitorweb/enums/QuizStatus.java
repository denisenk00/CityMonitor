package com.denysenko.citymonitorweb.enums;

public enum QuizStatus {

    PLANNED("Заплановано"),
    IN_PROGRESS("В процесі"),
    FINISHED("Завершено");

    private String title;

    QuizStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static QuizStatus getByTitle(String title) {
        for (QuizStatus status : values()) {
            if (status.getTitle().equals(title)) return status;
        }
        throw new EnumConstantNotPresentException(QuizStatus.class, title);
    }
}
