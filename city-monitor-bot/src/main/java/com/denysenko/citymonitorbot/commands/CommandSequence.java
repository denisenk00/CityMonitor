package com.denysenko.citymonitorbot.commands;

public interface CommandSequence<T> extends Command<T>{
    void executeNext(T t);
    void executePrevious(T t);
}
