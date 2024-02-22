package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.repositories.hibernate.FileRepository;
import com.denysenko.citymonitorweb.services.entity.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public File getFileByID(Long id) {
        Optional<com.denysenko.citymonitorweb.models.entities.File> fileOpt = fileRepository.findById(id);
        return fileOpt.orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти файл з id = " + id));
    }

}
