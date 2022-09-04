package com.denysenko.citymonitorbot.repositories.hibernate;

import com.denysenko.citymonitorbot.models.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppealDAO extends JpaRepository<Appeal, Integer> {
}
