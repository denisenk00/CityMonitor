package com.denysenko.citymonitorweb.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDTO {
    private Long id;
    private OptionDTO option;
    private Long polygonId;
    private Integer answersCnt;
}
