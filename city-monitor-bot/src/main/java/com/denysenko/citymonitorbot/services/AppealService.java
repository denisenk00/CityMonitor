package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.repositories.cache.AppealCacheRepository;
import com.denysenko.citymonitorbot.repositories.hibernate.AppealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class AppealService {

    private final AppealRepository appealRepository;
    private final AppealCacheRepository appealCacheRepository;

    public void saveAppeal(@NotNull Appeal appeal){
        appealRepository.save(appeal);
    }

    //Cache
    public Optional<Appeal> findAppealInCacheByChatId(@NotNull Long chatId){
        return appealCacheRepository.findAppealByChatId(chatId);
    }

    public void updateAppealInCacheByChatId(@NotNull Long chatId, @NotNull Appeal appeal){
        appealCacheRepository.saveAppealByChatId(chatId, appeal);
    }

    public void removeAppealByChatIdFromCache(@NotNull Long chatId){
        appealCacheRepository.removeAppealByChatId(chatId);
    }

}
