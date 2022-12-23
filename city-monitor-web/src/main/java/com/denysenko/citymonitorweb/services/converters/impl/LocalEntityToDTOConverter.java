package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.LocalDTO;
import com.denysenko.citymonitorweb.models.dto.LocationPointDTO;
import com.denysenko.citymonitorweb.models.entities.Local;
import com.denysenko.citymonitorweb.repositories.hibernate.LocalRepository;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class LocalEntityToDTOConverter implements EntityDTOConverter<Local, LocalDTO> {

    @Override
    public Local convertDTOToEntity(LocalDTO localDTO) throws ConversionFailedException {
        return null;
    }

    @Override
    public LocalDTO convertEntityToDTO(Local local) throws ConversionFailedException {
        Point point = local.getLocation();
        LocationPointDTO locationPointDTO = null;
        if(Objects.nonNull(point)){
            locationPointDTO = new LocationPointDTO(point.getX(), point.getY());
        }
        return LocalDTO.builder()
                .id(local.getId())
                .location(locationPointDTO)
                .chatId(local.getChatId())
                .isActive(local.isActive())
                .name(local.getName())
                .phone(local.getPhone())
                .build();
    }
}
