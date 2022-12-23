package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AppealRepository extends JpaRepository<Appeal, Long> {
    long countByStatusEquals(AppealStatus status);
    Page<Appeal> findAllByStatusIn(Set<AppealStatus> statuses, Pageable pageable);
}
