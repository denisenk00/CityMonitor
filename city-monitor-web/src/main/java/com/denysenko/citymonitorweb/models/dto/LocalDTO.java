package com.denysenko.citymonitorweb.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalDTO {

    private Long id;

    private Long chatId;

    private String name;

    private String phone;

    private LocationPointDTO location;

    private boolean isActive;
}
