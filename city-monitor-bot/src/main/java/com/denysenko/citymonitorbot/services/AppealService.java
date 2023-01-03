package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.repositories.cache.AppealCacheRepository;
import com.denysenko.citymonitorbot.repositories.hibernate.AppealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppealService {

    @Autowired
    private AppealRepository appealRepository;
    @Autowired
    private AppealCacheRepository appealCacheRepository;

    public void saveAppeal(Appeal appeal){
        if(appeal == null) throw new IllegalArgumentException("Appeal should not be NULL");
        appealRepository.save(appeal);
    }

    //Cache
    public Optional<Appeal> findAppealInCacheByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return appealCacheRepository.findAppealByChatId(chatId);
    }

    public void updateAppealInCacheByChatId(Long chatId, Appeal appeal){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        if(appeal == null) throw new IllegalArgumentException("Appeal should not be NULL");
        appealCacheRepository.saveAppealByChatId(chatId, appeal);
    }

    public void removeAppealByChatIdFromCache(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        appealCacheRepository.removeAppealByChatId(chatId);
    }

}
