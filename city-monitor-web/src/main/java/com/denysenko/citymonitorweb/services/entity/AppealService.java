package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface AppealService {
    long countOfUnreadAppeals();
    Page<Appeal> getPageByStatuses(int pageNumber, int size, Set<AppealStatus> statuses);
    void updateStatusById(Long id, AppealStatus appealStatus);
}
