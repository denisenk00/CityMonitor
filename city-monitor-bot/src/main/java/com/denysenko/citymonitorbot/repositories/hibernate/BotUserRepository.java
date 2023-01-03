package com.denysenko.citymonitorbot.repositories.hibernate;

import com.denysenko.citymonitorbot.models.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;


public interface BotUserRepository extends JpaRepository<BotUser, Long> {

    Optional<BotUser> findByChatId(Long chatId);
    boolean existsByChatId(Long chatId);
    boolean existsByPhone(String phone);

    @Transactional
    @Modifying
    @Query("update BotUser b set b.isActive = :isActive where b.chatId = :chatId")
    void changeActivity(@Param("chatId") Long chatId, @Param("isActive") boolean isActive);
}
