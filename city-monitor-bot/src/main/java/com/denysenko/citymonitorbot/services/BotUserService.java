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
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Log4j
@Service
@Validated
public class BotUserService {

    @Autowired
    private UserStateCacheRepository botUserStateCacheRepository;
    @Autowired
    private BotUserCacheRepository botUserCacheRepository;
    @Autowired
    private BotUserRepository botUserRepository;

    //Database
    public Optional<BotUser> findBotUserByChatId(@NotNull Long chatId){
        return botUserRepository.findByChatId(chatId);
    }

    public BotUser getBotUserByChatId(@NotNull Long chatId){
        return botUserRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("botUser with chatId = " + chatId + " was not found"));
    }

    public boolean userIsRegistered(@NotNull Long chatId){
        return botUserRepository.existsByChatId(chatId);
    }

    public void saveBotUserToDB(@NotNull BotUser botUser){
        botUserRepository.save(botUser);
    }

    public boolean existsUserByPhone(@NotBlank String phone){
        return botUserRepository.existsByPhone(phone);
    }

    public void deactivateBotUser(@NotNull Long chatId){
        botUserRepository.changeActivity(chatId, false);
    }

    //Cache BotUserState
    public Optional<BotStates> findBotStateByChatId(@NotNull Long chatId){
        return botUserStateCacheRepository.findBotStateByChatId(chatId);
    }

    public void updateBotStateByChatId(@NotNull Long chatId, @NotNull BotStates botStates){
        botUserStateCacheRepository.updateStateByChatId(chatId, botStates);
    }

    public void removeBotStateByChatId(@NotNull Long chatId){
        botUserStateCacheRepository.removeStateByChatId(chatId);
    }

    //Cache BotUser
    public Optional<BotUser> findBotUserInCacheByChatId(@NotNull Long chatId){
        return botUserCacheRepository.findBotUserByChatId(chatId);
    }

    public void updateBotUserInCacheByChatId(@NotNull Long chatId, @NotNull BotUser botUser){
        botUserCacheRepository.saveBotUserByChatId(chatId, botUser);
    }

    public void removeBotUserByChatIdFromCache(@NotNull Long chatId){
        botUserCacheRepository.removeBotUserByChatId(chatId);
    }

}
