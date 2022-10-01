package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.repositories.hibernate.FileRepository;
import com.denysenko.citymonitorweb.services.EntityDTOConverter;
import com.denysenko.citymonitorweb.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public File convertDTOToEntity(FileDTO fileDTO) {
        return File.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getName())
                .content(fileDTO.getBytes())
                .build();
    }

    @Override
    public FileDTO convertEntityToDTO(File file) {
        return FileDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .content(file.getContent())
                .build();
    }
}
