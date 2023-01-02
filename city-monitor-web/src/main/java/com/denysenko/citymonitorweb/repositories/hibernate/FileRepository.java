package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileID(String tgFileId);
}
