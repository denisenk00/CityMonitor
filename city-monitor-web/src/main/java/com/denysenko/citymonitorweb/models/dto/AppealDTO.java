package com.denysenko.citymonitorweb.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppealDTO {
    private Long id;
    private String text;
    private String status;
    private LocalDTO local;
    private LocalDateTime postDate;
    private LocationPointDTO locationPointDTO;
    private List<FileDTO> fileDTOs;
}
