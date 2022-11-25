package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.repositories.hibernate.FileRepository;
import com.denysenko.citymonitorweb.services.entity.FileService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class FileServiceImpl implements FileService{
    @Autowired
    private FileRepository fileRepository;

    public File getIOFileByID(Long id){
        if(id == null) throw new IllegalArgumentException("File id should not be NULL");
        Optional<com.denysenko.citymonitorweb.models.entities.File> fileOpt = fileRepository.findById(id);
        return fileOpt.orElseThrow(()-> new NoSuchElementException());
    }

}
