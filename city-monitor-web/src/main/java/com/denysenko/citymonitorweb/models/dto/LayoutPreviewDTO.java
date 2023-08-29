package com.denysenko.citymonitorweb.models.dto;

import com.denysenko.citymonitorweb.enums.LayoutStatus;


public interface LayoutPreviewDTO {
    Long getId();
    String getName();
    LayoutStatus getStatus();
}
