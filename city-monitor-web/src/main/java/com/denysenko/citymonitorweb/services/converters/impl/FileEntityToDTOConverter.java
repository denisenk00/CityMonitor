package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.repositories.hibernate.FileRepository;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import com.denysenko.citymonitorweb.services.telegram.TelegramService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class FileEntityToDTOConverter implements EntityDTOConverter<File, FileDTO> {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public File convertDTOToEntity(FileDTO fileDTO) throws ConversionFailedException {
        File file;
        if(fileDTO.getId() != null){
            Optional<File> fileOptional = fileRepository.findById(fileDTO.getId());
            file = fileOptional.orElse(new File());
        }else {
            file = new File();
        }
        file.setId(fileDTO.getId());
        file.setContent(fileDTO.getContent());
        file.setFileID(fileDTO.getTgFileId());
        file.setName(file.getName());
        file.setContent(fileDTO.getContent());
        return file;
    }

    @Override
    public FileDTO convertEntityToDTO(File file) throws ConversionFailedException {
        return FileDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .tgFileId(file.getFileID())
                .content(file.getContent())
                .build();
    }
}
