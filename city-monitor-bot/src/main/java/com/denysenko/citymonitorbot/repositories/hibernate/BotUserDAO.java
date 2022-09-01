package com.denysenko.citymonitorbot.repositories.hibernate;

import com.denysenko.citymonitorbot.models.entities.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BotUserDAO extends JpaRepository<BotUser, Long> {
    Optional<BotUser> findByChatId(Long chatId);
    boolean existsByChatId(Long chatId);
}
