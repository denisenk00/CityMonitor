package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class QuizDTO {
    private Long id;
    private String title;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    private Date startDate;
    private Date endDate;
    private Long layoutId;
    private List<FileDTO> fileDTOs;
    private List<OptionDTO> optionDTOs;
}
