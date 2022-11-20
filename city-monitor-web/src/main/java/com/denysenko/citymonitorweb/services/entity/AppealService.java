package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;

public interface AppealService {
    long countOfUnreadAppeals();
}
