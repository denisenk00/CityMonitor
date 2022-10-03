package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.repositories.hibernate.FileRepository;
import com.denysenko.citymonitorweb.services.FileService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;


@Service
public class FileServiceImpl implements FileService{
    @Autowired
    private FileRepository fileRepository;

    @Override
    @SneakyThrows
    public FileDTO convertMultipartFileToDTO(MultipartFile multipartFile) {
        if(multipartFile == null) throw new NullPointerException();
        return FileDTO.builder()
                .name(multipartFile.getName())
                .content(multipartFile.getBytes())
                .build();
    }

    @Override
    public List<FileDTO> convertListOfMultipartFileToDTO(List<MultipartFile> multipartFiles) {
        if(multipartFiles == null) throw new NullPointerException();
        List<FileDTO> fileList = new LinkedList<>();
        multipartFiles.forEach(mFile -> fileList.add(convertMultipartFileToDTO(mFile)));
        return fileList;
    }

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
                .content(file.getContent())
                .build();
    }
}
