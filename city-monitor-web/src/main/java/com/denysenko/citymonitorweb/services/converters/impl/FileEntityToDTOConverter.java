package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.repositories.hibernate.FileRepository;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FileEntityToDTOConverter implements EntityDTOConverter<File, FileDTO> {

    private final FileRepository fileRepository;

    @Override
    public File convertDTOToEntity(FileDTO fileDTO) throws ConversionFailedException {
        try {
            File file;
            if (fileDTO.getId() != null) {
                Optional<File> fileOptional = fileRepository.findById(fileDTO.getId());
                file = fileOptional.orElse(new File());
            } else {
                file = new File();
            }
            file.setId(fileDTO.getId());
            file.setContent(fileDTO.getContent());
            file.setFileID(fileDTO.getTgFileId());
            file.setName(fileDTO.getName());
            file.setContent(fileDTO.getContent());
            return file;
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(fileDTO), TypeDescriptor.valueOf(File.class), null, e);
        }
    }

    @Override
    public FileDTO convertEntityToDTO(File file) throws ConversionFailedException {
        try {
            return FileDTO.builder()
                    .id(file.getId())
                    .name(file.getName())
                    .tgFileId(file.getFileID())
                    .content(file.getContent())
                    .build();
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(file), TypeDescriptor.valueOf(FileDTO.class), null, e);
        }
    }
}
