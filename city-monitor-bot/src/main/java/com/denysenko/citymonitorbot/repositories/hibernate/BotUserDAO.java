package com.denysenko.citymonitorbot.repositories.hibernate;

import com.denysenko.citymonitorbot.models.entities.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BotUserDAO extends JpaRepository<BotUser, Long> {
}
