package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionDTO {
    private Long id;
    @NotEmpty(message = "Варіант відповіді не може бути пустим")
    @Length(max = 500, message = "Максимальна кількість символів - 500")
    private String title;
}
