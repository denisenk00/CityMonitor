package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

@Service
public class AppealEntityToDTOConverter implements EntityDTOConverter<Appeal, AppealDTO> {
    @Override
    public Appeal convertDTOToEntity(AppealDTO appealDTO) throws ConversionFailedException {
        return null;
    }

    @Override
    public AppealDTO convertEntityToDTO(Appeal appeal) throws ConversionFailedException {
        return null;
    }
}
