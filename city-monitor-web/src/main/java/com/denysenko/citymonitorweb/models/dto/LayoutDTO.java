package com.denysenko.citymonitorweb.models.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LayoutDTO {
    private Long id;
    @NotEmpty
    @Size(min = 10, max = 100)
    private String name;
    private String status;
    @NotEmpty
    private String polygonsGeoJson;;
}
