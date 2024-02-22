package com.denysenko.citymonitorbot.repositories.cache;

import com.denysenko.citymonitorbot.enums.BotStates;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserStateCacheRepository {

    private final Map<Long, BotStates> userStates = new ConcurrentHashMap<>(1000);

    public Optional<BotStates> findBotStateByChatId(Long chatId) {
        return Optional.ofNullable(userStates.get(chatId));
    }

    public void updateStateByChatId(Long chatId, BotStates botStates) {
        userStates.put(chatId, botStates);
    }

    public void removeStateByChatId(Long chatId) {
        userStates.remove(chatId);
    }
}
