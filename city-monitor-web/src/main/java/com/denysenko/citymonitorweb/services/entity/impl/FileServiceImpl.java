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


@Service
public class FileServiceImpl implements FileService{
    @Autowired
    private FileRepository fileRepository;


}
