package com.denysenko.citymonitorbot.services.entity;

import com.denysenko.citymonitorbot.models.BotUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface BotUserService {

    Optional<BotUser> findBotUserByChatId(@NotNull Long chatId);

    BotUser getBotUserByChatId(@NotNull Long chatId);

    boolean userIsRegistered(@NotNull Long chatId);

    void saveBotUserToDB(@NotNull BotUser botUser);

    boolean existsUserByPhone(@NotBlank String phone);

    void deactivateBotUser(@NotNull Long chatId);

    void saveToDBAndCleanCache(Long chatId);

}
