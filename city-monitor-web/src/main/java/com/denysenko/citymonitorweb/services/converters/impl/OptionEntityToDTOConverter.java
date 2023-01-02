package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

@Service
public class OptionEntityToDTOConverter implements EntityDTOConverter<Option, OptionDTO> {
    @Override
    public Option convertDTOToEntity(OptionDTO optionDTO) {
        try {
            return Option.builder()
                    .id(optionDTO.getId())
                    .title(optionDTO.getTitle())
                    .build();
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(optionDTO), TypeDescriptor.valueOf(Option.class), null, e);
        }
    }

    @Override
    public OptionDTO convertEntityToDTO(Option option) {
        try {
            return OptionDTO.builder()
                    .id(option.getId())
                    .title(option.getTitle())
                    .build();
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(option), TypeDescriptor.valueOf(Option.class), null, e);
        }
    }
}
