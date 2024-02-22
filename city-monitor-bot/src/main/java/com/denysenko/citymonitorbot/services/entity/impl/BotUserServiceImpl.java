package com.denysenko.citymonitorbot.services.entity.impl;

import com.denysenko.citymonitorbot.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorbot.models.BotUser;
import com.denysenko.citymonitorbot.repositories.hibernate.BotUserRepository;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@Service
@Validated
@Transactional(readOnly = true)
public class BotUserServiceImpl implements BotUserService {

    private final CacheManager cacheManager;
    private final BotUserRepository botUserRepository;

    @Override
    public Optional<BotUser> findBotUserByChatId(@NotNull Long chatId) {
        return botUserRepository.findByChatId(chatId);
    }

    @Override
    public BotUser getBotUserByChatId(@NotNull Long chatId) {
        return botUserRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("botUser with chatId = " + chatId + " was not found"));
    }

    @Override
    public boolean userIsRegistered(@NotNull Long chatId) {
        return botUserRepository.existsByChatId(chatId);
    }

    @Override
    @Transactional
    public void saveBotUserToDB(@NotNull BotUser botUser) {
        botUserRepository.save(botUser);
    }

    @Override
    public boolean existsUserByPhone(@NotBlank String phone) {
        return botUserRepository.existsByPhone(phone);
    }

    @Override
    @Transactional
    public void deactivateBotUser(@NotNull Long chatId) {
        botUserRepository.changeActivity(chatId, false);
    }

    @Override
    @Transactional
    public void saveToDBAndCleanCache(Long chatId) {
        log.info("Saving user data to remote database and clearing caches: chatId = " + chatId);
        Optional<BotUser> cachedUser = cacheManager.findBotUserByChatId(chatId);
        cachedUser.get().setActive(true);
        saveBotUserToDB(cachedUser.get());
        cacheManager.removeBotUserByChatId(chatId);
    }

}
