package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.AbstractMap;
import java.util.Set;

@Validated
public interface AppealService {
    long countOfUnreadAppeals();
    Page<AppealDTO> getPageByStatuses(int pageNumber, int size, @NotEmpty Set<AppealStatus> statuses);
    void updateStatusById(@NotNull Long id, @NotNull AppealStatus appealStatus);
    AbstractMap.SimpleImmutableEntry<String, byte[]> getAppealFileContent(Long fileId);
}
