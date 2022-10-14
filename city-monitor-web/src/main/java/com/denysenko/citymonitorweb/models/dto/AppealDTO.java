package com.denysenko.citymonitorweb.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AppealDTO {
    private Long id;
    private String text;
    private String status;
    private LocalDateTime postDate;
    private LocationPointDTO locationPointDTO;
    private List<FileDTO> fileDTOs;
}
