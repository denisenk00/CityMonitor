package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.repositories.cache.BotUserRepository;
import com.denysenko.citymonitorbot.repositories.cache.BotUserStateRepository;
import com.denysenko.citymonitorbot.repositories.hibernate.BotUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BotUserService {

    @Autowired
    private BotUserStateRepository botUserStateCacheRepository;
    @Autowired
    private BotUserRepository botUserCacheRepository;
    @Autowired
    private BotUserDAO botUserDAO;

    //Database
    public Optional<BotUser> findBotUserByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserDAO.findByChatId(chatId);
    }

    public boolean userIsRegistered(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserDAO.existsByChatId(chatId);
    }

    public void saveBotUserToDB(BotUser botUser){
        if(botUser == null) throw new IllegalArgumentException("botUser should not be NULL");
        botUserDAO.save(botUser);
    }
    public boolean existsUserByPhone(String phone){
        if(phone == null) throw new IllegalArgumentException("phone should not be NULL");
        return botUserDAO.existsByPhone(phone);
    }

    public void deactivateBotUser(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        botUserDAO.changeActivity(chatId, false);
    }

    //Cache BotUserState
    public Optional<BotStates> findBotStateByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserStateCacheRepository.findBotStateByChatId(chatId);
    }

    public void updateBotStateByChatId(Long chatId, BotStates botStates){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        if(botStates == null) throw new IllegalArgumentException("botStates should not be NULL");
        botUserStateCacheRepository.updateStateByChatId(chatId, botStates);
    }

    public void removeBotStateByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        botUserStateCacheRepository.removeStateByChatId(chatId);
    }

    //Cache BotUser
    public Optional<BotUser> findBotUserInCacheByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserCacheRepository.findBotUserByChatId(chatId);
    }

    public void updateBotUserInCacheByChatId(Long chatId, BotUser botUser){
        if(botUser == null) throw new IllegalArgumentException("botUser should not be NULL");
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        botUserCacheRepository.saveBotUserByChatId(chatId, botUser);
    }

    public void removeBotUserByChatIdFromCache(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        botUserCacheRepository.removeBotUserByChatId(chatId);
    }

}
