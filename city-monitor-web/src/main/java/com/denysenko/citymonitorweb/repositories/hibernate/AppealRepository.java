package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppealRepository extends JpaRepository<Appeal, Long> {
    long countByStatusEquals(AppealStatus status);
}
