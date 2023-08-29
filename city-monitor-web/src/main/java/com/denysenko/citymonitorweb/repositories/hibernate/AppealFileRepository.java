package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.AppealFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppealFileRepository extends JpaRepository<AppealFile, Long> {
}
