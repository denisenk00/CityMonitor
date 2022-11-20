package com.denysenko.citymonitorweb.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Permission {
    APPEALS_READ("appeals:read"),
    APPEALS_WRITE("appeals:write"),
    LAYOUTS_READ("layouts:read"),
    LAYOUTS_WRITE("layouts:write"),
    QUIZZES_READ("quizzes:read"),
    QUIZZES_WRITE("quizzes:write"),
    USERS_READ("users:read"),
    USERS_WRITE("users:write");

    private String title;

    Permission(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public static Set<Permission> getAll(){
        return Arrays.stream(Permission.values()).collect(Collectors.toSet());
    }
}
