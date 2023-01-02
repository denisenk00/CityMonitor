package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private Long id;
    @NotBlank(message = "Назва файлу не може бути пустою")
    private String name;
    private String tgFileId;
    private byte[] content;

    @AssertTrue(message = "Файл повинен містити наповнення")
    public boolean containsContentOrTgFileId(){
        return (Objects.nonNull(tgFileId) && !tgFileId.isEmpty()) || content.length > 0;
    }
}
