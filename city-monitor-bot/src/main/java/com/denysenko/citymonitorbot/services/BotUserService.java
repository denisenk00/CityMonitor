package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.repositories.cache.BotUserCacheRepository;
import com.denysenko.citymonitorbot.repositories.cache.UserStateCacheRepository;
import com.denysenko.citymonitorbot.repositories.hibernate.BotUserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j
@Service
public class BotUserService {

    @Autowired
    private UserStateCacheRepository botUserStateCacheRepository;
    @Autowired
    private BotUserCacheRepository botUserCacheRepository;
    @Autowired
    private BotUserRepository botUserRepository;

    //Database
    public Optional<BotUser> findBotUserByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserRepository.findByChatId(chatId);
    }

    public BotUser getBotUserByChatId(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("botUser with chatId = " + chatId + " was not found"));
    }

    public boolean userIsRegistered(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        return botUserRepository.existsByChatId(chatId);
    }

    public void saveBotUserToDB(BotUser botUser){
        if(botUser == null) throw new IllegalArgumentException("botUser should not be NULL");
        botUserRepository.save(botUser);
    }
    public boolean existsUserByPhone(String phone){
        if(phone == null) throw new IllegalArgumentException("phone should not be NULL");
        return botUserRepository.existsByPhone(phone);
    }

    public void deactivateBotUser(Long chatId){
        if(chatId == null) throw new IllegalArgumentException("ChatId should not be NULL");
        botUserRepository.changeActivity(chatId, false);
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
