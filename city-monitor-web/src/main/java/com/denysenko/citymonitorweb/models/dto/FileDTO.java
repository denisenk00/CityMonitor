package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private Long id;
    @NotBlank(message = "Назва файлу не може бути пустою")
    private String name;
    @NotNull(message = "Файл повинен містити наповнення")
    private byte[] content;
}
