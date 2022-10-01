package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.repositories.hibernate.OptionRepository;
import com.denysenko.citymonitorweb.services.EntityDTOConverter;
import com.denysenko.citymonitorweb.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    private OptionRepository optionRepository;

    @Override
    public Option convertDTOToEntity(OptionDTO optionDTO) {
        return Option.builder()
                .optionId(optionDTO.getId())
                .title(optionDTO.getTitle())
                .build();
    }

    @Override
    public OptionDTO convertEntityToDTO(Option option) {
        return OptionDTO.builder()
                .id(option.getOptionId())
                .title(option.getTitle())
                .build();
    }

}
