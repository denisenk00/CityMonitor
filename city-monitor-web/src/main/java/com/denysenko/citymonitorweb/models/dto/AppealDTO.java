package com.denysenko.citymonitorweb.models.dto;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import org.locationtech.jts.geom.Point;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;


public interface AppealDTO {
    Long getId();
    String getText();
    AppealStatus getStatus();
    LocalDTO getLocal();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime getPostDate();
    Point getLocationPoint();
    List<AppealFileDTO> getFiles();


    interface LocalDTO {
        String getName();
        String getPhone();
    }

    interface AppealFileDTO {
        Long getId();
        String getName();
        String getTgFileId();
    }
}
