package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.LocalDTO;
import com.denysenko.citymonitorweb.models.dto.LocationPointDTO;
import com.denysenko.citymonitorweb.models.entities.Local;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.locationtech.jts.geom.Point;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LocalEntityToDTOConverter implements EntityDTOConverter<Local, LocalDTO> {

    @Override
    public Local convertDTOToEntity(LocalDTO localDTO) {
        throw new UnsupportedOperationException("Метод convertDTOToEntity(LocalDTO localDTO) не реалізовано.");
    }

    @Override
    public LocalDTO convertEntityToDTO(Local local) throws ConversionFailedException{
        try {
            Point point = local.getLocation();
            LocationPointDTO locationPointDTO = null;
            if (Objects.nonNull(point)) {
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
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(local), TypeDescriptor.valueOf(LocalDTO.class), null, e);
        }
    }
}
