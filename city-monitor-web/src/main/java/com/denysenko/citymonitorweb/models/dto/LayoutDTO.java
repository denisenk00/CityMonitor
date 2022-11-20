package com.denysenko.citymonitorweb.models.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LayoutDTO {
    private Long id;
    private String name;
    private String status;
    private String polygonsGeoJson;;
}
