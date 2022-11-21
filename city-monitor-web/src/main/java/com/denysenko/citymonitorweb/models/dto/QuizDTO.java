package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDTO {
    private Long id;
    @NotEmpty(message = "Тема опитування не може бути пустою")
    @Length(min = 10, max = 100, message = "Мінімальна кількість символів - 10, максимальна - 100")
    private String title;
    @NotEmpty(message = "Опис опитування не може бути пустим")
    @Length(min = 30, max = 1000, message = "Мінімальна кількість символів - 30, максимальна - 1000")
    private String description;
    private String status;
    private boolean startImmediate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent(message = "Дата початку опитування не може бути в минулому")
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Визначте дату завершення опитування")
    @Future(message = "Дата завершення не може бути в минулому")
    private LocalDateTime endDate;
    private LayoutDTO layoutDTO;
    private List<FileDTO> fileDTOs;
    @NotNull
    @Valid
    @Size(min = 2, message = "Повинно бути мінімум 2 варіанти відповіді")
    private List<OptionDTO> optionDTOs;
}
