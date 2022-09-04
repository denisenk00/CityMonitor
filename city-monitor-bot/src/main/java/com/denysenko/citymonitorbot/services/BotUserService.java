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
        return botUserDAO.findByChatId(chatId);
    }

    public boolean userIsRegistered(Long chatId){
        return botUserDAO.existsById(chatId);
    }

    public void saveBotUserToDB(BotUser botUser){
        botUserDAO.save(botUser);
    }
    public boolean existsUserByPhone(String phone){
        return botUserDAO.existsByPhone(phone);
    }

    public boolean deactivateBotUser(Long chatId){
        Optional<BotUser> botUser = findBotUserByChatId(chatId);
        if(botUser.isPresent()){
            botUser.get().setActive(false);
            saveBotUserToDB(botUser.get());
            return true;
        }else return false;
    }

    //Cache BotUserState
    public Optional<BotStates> findBotStateByChatId(Long chatId){
        return botUserStateCacheRepository.findBotStateByChatId(chatId);
    }

    public void updateBotStateByChatId(Long chatId, BotStates botStates){
        botUserStateCacheRepository.updateStateByChatId(chatId, botStates);
    }

    public void removeBotStateByChatId(Long chatId){
        botUserStateCacheRepository.removeStateByChatId(chatId);
    }

    //Cache BotUser
    public Optional<BotUser> findBotUserInCacheByChatId(Long chatId){
        return botUserCacheRepository.findBotUserByChatId(chatId);
    }

    public void updateBotUserInCacheByChatId(Long chatId, BotUser botUser){
        botUserCacheRepository.saveBotUserByChatId(chatId, botUser);
    }

    public void removeBotUserByChatIdFromCache(Long chatId){
        botUserCacheRepository.removeBotUserByChatId(chatId);
    }

}
