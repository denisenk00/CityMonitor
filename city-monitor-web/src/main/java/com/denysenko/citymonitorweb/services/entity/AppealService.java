package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Validated
public interface AppealService {
    long countOfUnreadAppeals();
    Page<Appeal> getPageByStatuses(int pageNumber, int size, @NotEmpty Set<AppealStatus> statuses);
    void updateStatusById(@NotNull Long id, @NotNull AppealStatus appealStatus);
}
