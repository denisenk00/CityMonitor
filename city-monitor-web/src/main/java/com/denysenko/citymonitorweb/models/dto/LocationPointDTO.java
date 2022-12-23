package com.denysenko.citymonitorweb.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LocationPointDTO {
    private double latitude;
    private double longitude;
}
