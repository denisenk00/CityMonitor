package com.denysenko.citymonitorbot.commands;

public interface Command<T> {
    void execute(T t);
}
