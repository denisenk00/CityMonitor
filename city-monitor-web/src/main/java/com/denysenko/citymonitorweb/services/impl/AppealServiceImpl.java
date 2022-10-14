package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.services.AppealService;
import org.springframework.core.convert.ConversionFailedException;

public class AppealServiceImpl implements AppealService {
    @Override
    public Appeal convertDTOToEntity(AppealDTO appealDTO) throws ConversionFailedException {
        return null;
    }

    @Override
    public AppealDTO convertEntityToDTO(Appeal appeal) throws ConversionFailedException {
        return null;
    }
}
