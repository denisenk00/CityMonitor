package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

@Log4j
@Service
public class MultiFileToDTOConverter {

    public FileDTO convertMultipartFileToDTO(MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) throw new FileNotFoundException("Об'єкт файлу пустий.");
            return FileDTO.builder()
                    .name(multipartFile.getOriginalFilename())
                    .content(multipartFile.getBytes())
                    .build();
        } catch (Exception e) {
            throw new ConversionFailedException(TypeDescriptor.forObject(multipartFile), TypeDescriptor.valueOf(FileDTO.class), null, e);
        }
    }

    public List<FileDTO> convertListOfMultipartFileToDTO(List<MultipartFile> multipartFiles) {
        try {
            List<FileDTO> fileList = new LinkedList<>();
            multipartFiles.forEach(mFile -> {
                fileList.add(convertMultipartFileToDTO(mFile));
            });
            return fileList;
        } catch (Exception e) {
            throw new ConversionFailedException(
                    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(MultipartFile.class)),
                    TypeDescriptor.valueOf(FileDTO.class), null, e);
        }

    }
}
