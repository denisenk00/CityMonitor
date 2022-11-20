package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.stereotype.Service;

@Service
public class OptionEntityToDTOConverter implements EntityDTOConverter<Option, OptionDTO> {
    @Override
    public Option convertDTOToEntity(OptionDTO optionDTO) {
        return Option.builder()
                .id(optionDTO.getId())
                .title(optionDTO.getTitle())
                .build();
    }

    @Override
    public OptionDTO convertEntityToDTO(Option option) {
        return OptionDTO.builder()
                .id(option.getId())
                .title(option.getTitle())
                .build();
    }
}
