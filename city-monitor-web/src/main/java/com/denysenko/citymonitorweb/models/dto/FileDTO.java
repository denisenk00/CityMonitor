package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private Long id;
    private String name;
    private String tgFileId;
    private byte[] content;
}
