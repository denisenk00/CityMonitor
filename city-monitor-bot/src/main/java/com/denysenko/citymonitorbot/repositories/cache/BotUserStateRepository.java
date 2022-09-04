package com.denysenko.citymonitorbot.repositories.cache;

import com.denysenko.citymonitorbot.enums.BotStates;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BotUserStateRepository {

    private final Map<Long, BotStates> userStates = new HashMap<>();

    public Optional<BotStates> findBotStateByChatId(Long chatId){
        return Optional.ofNullable(userStates.get(chatId));
    }

    public void updateStateByChatId(Long chatId, BotStates botStates){
        userStates.put(chatId, botStates);
    }

    public void removeStateByChatId(Long chatId){
        userStates.remove(chatId);
    }
}
