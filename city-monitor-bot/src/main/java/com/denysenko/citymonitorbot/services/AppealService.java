package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.repositories.cache.AppealRepository;
import com.denysenko.citymonitorbot.repositories.hibernate.AppealDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppealService {

    @Autowired
    private AppealDAO appealDAO;
    @Autowired
    private AppealRepository appealCacheRepository;

    public void saveAppeal(Appeal appeal){
        if(appeal == null) throw new IllegalArgumentException("Appeal should not be NULL");
        appealDAO.save(appeal);
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
