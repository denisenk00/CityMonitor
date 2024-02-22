package com.denysenko.citymonitorbot.services.impl;

import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.repositories.cache.AppealCacheRepository;
import com.denysenko.citymonitorbot.repositories.cache.BotUserCacheRepository;
import com.denysenko.citymonitorbot.repositories.cache.UserStateCacheRepository;
import com.denysenko.citymonitorbot.services.CacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheManagerImpl implements CacheManager {

    private final UserStateCacheRepository botUserStateCacheRepository;
    private final BotUserCacheRepository botUserCacheRepository;
    private final AppealCacheRepository appealCacheRepository;

    //BotStates
    @Override
    public Optional<BotStates> findBotStateByChatId(@NotNull Long chatId) {
        return botUserStateCacheRepository.findBotStateByChatId(chatId);
    }

    @Override
    public void updateBotStateByChatId(@NotNull Long chatId, @NotNull BotStates botStates) {
        botUserStateCacheRepository.updateStateByChatId(chatId, botStates);
    }

    @Override
    public void removeBotStateByChatId(@NotNull Long chatId) {
        botUserStateCacheRepository.removeStateByChatId(chatId);
    }

    //BotUser
    @Override
    public Optional<BotUser> findBotUserByChatId(@NotNull Long chatId) {
        return botUserCacheRepository.findBotUserByChatId(chatId);
    }

    @Override
    public void saveBotUserByChatId(@NotNull Long chatId, @NotNull BotUser botUser) {
        botUserCacheRepository.saveBotUserByChatId(chatId, botUser);
    }

    @Override
    public void removeBotUserByChatId(@NotNull Long chatId) {
        botUserCacheRepository.removeBotUserByChatId(chatId);
    }

    //Appeals
    @Override
    public Optional<Appeal> findAppealByChatId(@NotNull Long chatId) {
        return appealCacheRepository.findAppealByChatId(chatId);
    }

    @Override
    public void removeAppealByChatId(@NotNull Long chatId) {
        appealCacheRepository.removeAppealByChatId(chatId);
    }

    @Override
    public void createEmptyAppeal(@NotNull Long chatId) {
        appealCacheRepository.saveAppealByChatId(chatId, new Appeal());
    }

}
