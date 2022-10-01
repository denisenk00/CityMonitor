package com.denysenko.citymonitorweb.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class LayoutDTO {
    private Long id;
    private String name;
    private String status;

}
