package com.denysenko.citymonitorbot.repositories.cache;

import com.denysenko.citymonitorbot.models.Appeal;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AppealCacheRepository {

    private final Map<Long, Appeal> cachedAppeals = new HashMap<>();

    public Optional<Appeal> findAppealByChatId(Long chatId){
        return Optional.ofNullable(cachedAppeals.get(chatId));
    }

    public void saveAppealByChatId(Long chatId, Appeal appeal){
        cachedAppeals.put(chatId, appeal);
    }

    public void removeAppealByChatId(Long chatId){
        cachedAppeals.remove(chatId);
    }

}
