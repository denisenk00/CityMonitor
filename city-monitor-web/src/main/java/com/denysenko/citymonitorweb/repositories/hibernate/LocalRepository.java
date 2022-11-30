package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.Local;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocalRepository extends JpaRepository<Local, Long> {

}

