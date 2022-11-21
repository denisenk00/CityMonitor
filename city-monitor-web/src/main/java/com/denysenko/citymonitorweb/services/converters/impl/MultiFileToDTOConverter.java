package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

@Service
public class MultiFileToDTOConverter {
    @SneakyThrows
    public FileDTO convertMultipartFileToDTO(MultipartFile multipartFile) throws FileNotFoundException{
        if(multipartFile == null) throw new NullPointerException();
        if(multipartFile.isEmpty()) throw new FileNotFoundException();
        return FileDTO.builder()
                .name(multipartFile.getName())
                .content(multipartFile.getBytes())
                .build();
    }

    public List<FileDTO> convertListOfMultipartFileToDTO(List<MultipartFile> multipartFiles) {
        if(multipartFiles == null) throw new NullPointerException();
        List<FileDTO> fileList = new LinkedList<>();
        multipartFiles.forEach(mFile -> {
            try {
                fileList.add(convertMultipartFileToDTO(mFile));
            } catch (FileNotFoundException e) {

            }
        });
        return fileList;
    }
}
