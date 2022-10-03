package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.entities.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService extends EntityDTOConverter<File, FileDTO>{
    FileDTO convertMultipartFileToDTO(MultipartFile multipartFile);
    List<FileDTO> convertListOfMultipartFileToDTO(List<MultipartFile> multipartFiles);

}
