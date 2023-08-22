package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.models.BotUser;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CacheManager {

    //BotStates
    Optional<BotStates> findBotStateByChatId(@NotNull Long chatId);

    void updateBotStateByChatId(@NotNull Long chatId, @NotNull BotStates botStates);

    void removeBotStateByChatId(@NotNull Long chatId);

    //Cache BotUser
    Optional<BotUser> findBotUserByChatId(@NotNull Long chatId);

    void saveBotUserByChatId(@NotNull Long chatId, @NotNull BotUser botUser);

    void removeBotUserByChatId(@NotNull Long chatId);

    //Appeals
    Optional<Appeal> findAppealByChatId(@NotNull Long chatId);

    void removeAppealByChatId(@NotNull Long chatId);

    void createEmptyAppeal(@NotNull Long chatId);
}
