package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import lombok.SneakyThrows;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Service
public class FileEntityToDTOConverter implements EntityDTOConverter<File, FileDTO> {

    @Override
    public File convertDTOToEntity(FileDTO fileDTO) throws ConversionFailedException {
        return File.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getName())
                .content(fileDTO.getContent())
                .build();
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
