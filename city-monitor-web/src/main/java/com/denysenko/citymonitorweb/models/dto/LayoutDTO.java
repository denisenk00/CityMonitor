package com.denysenko.citymonitorweb.models.dto;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LayoutDTO {
    private Long id;
    @NotBlank
    @Size(min = 10, max = 100)
    private String name;
    private LayoutStatus status;
    @NotBlank
    private String polygonsGeoJson;
}
