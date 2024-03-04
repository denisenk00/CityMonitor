package com.denysenko.citymonitorbot.repositories.cache;

import com.denysenko.citymonitorbot.models.Appeal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppealCacheRepositoryTest {

    @Test
    public void saveAndFindPositiveTest(){
        Long chatId = 14L;
        var appeal = new Appeal();
        var appealRepo = new AppealCacheRepository();
        appealRepo.saveAppealByChatId(chatId, appeal);
        var result = appealRepo.findAppealByChatId(chatId);
        assertTrue(result.isPresent());
        assertEquals(appeal, result.get());
    }

    @Test
    public void findNegativeTest(){
        Long existingEntityId = 14L;
        Long notExistingEntityId = 15L;
        var appeal = new Appeal();
        var appealRepo = new AppealCacheRepository();
        appealRepo.saveAppealByChatId(existingEntityId, appeal);
        var result = appealRepo.findAppealByChatId(notExistingEntityId);
        assertFalse(result.isPresent());
    }

    @Test
    public void removePositiveTest(){
        Long chatId = 14L;
        var appeal = new Appeal();
        var appealRepo = new AppealCacheRepository();
        appealRepo.saveAppealByChatId(chatId, appeal);
        appealRepo.removeAppealByChatId(chatId);
        var result = appealRepo.findAppealByChatId(chatId);
        assertFalse(result.isPresent());
    }

}
