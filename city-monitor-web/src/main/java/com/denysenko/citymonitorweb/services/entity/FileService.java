package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    File getIOFileByID(Long id);

}
