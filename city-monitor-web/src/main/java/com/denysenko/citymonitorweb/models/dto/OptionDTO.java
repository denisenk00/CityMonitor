package com.denysenko.citymonitorweb.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class OptionDTO {
    private Long id;
    private String title;
}
