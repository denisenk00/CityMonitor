package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppealRepository extends JpaRepository<Appeal, Long> {

}
