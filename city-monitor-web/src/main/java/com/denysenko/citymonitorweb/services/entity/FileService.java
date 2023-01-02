package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.File;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public interface FileService {

    File getFileByID(@NotNull Long id);
    File getFileByTgFileId(@NotBlank String tgFileId);
}
