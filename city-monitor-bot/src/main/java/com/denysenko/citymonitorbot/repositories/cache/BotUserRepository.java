package com.denysenko.citymonitorbot.repositories.cache;

import com.denysenko.citymonitorbot.models.BotUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BotUserRepository {

    private final Map<Long, BotUser> cachedUsers = new HashMap<>();

    public Optional<BotUser> findBotUserByChatId(Long chatId){
        return Optional.ofNullable(cachedUsers.get(chatId));
    }

    public void saveBotUserByChatId(Long chatId, BotUser botUser){
        cachedUsers.put(chatId, botUser);
    }

    public void removeBotUserByChatId(Long chatId){
        cachedUsers.remove(chatId);
    }
}
