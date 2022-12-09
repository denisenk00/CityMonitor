package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.ResultDTO;
import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

@Service
public class ResultEntityToDTOConverter implements EntityDTOConverter<Result, ResultDTO> {

    @Autowired
    private OptionEntityToDTOConverter optionConverter;

    @Override
    public Result convertDTOToEntity(ResultDTO resultDTO) throws ConversionFailedException {
        return null;
    }

    @Override
    public ResultDTO convertEntityToDTO(Result result) throws ConversionFailedException {
        OptionDTO optionDTO = optionConverter.convertEntityToDTO(result.getOption());
        return ResultDTO.builder()
                .id(result.getId())
                .option(optionDTO)
                .polygonId(result.getPolygon().getId())
                .answersCnt(result.getAnswersCount())
                .build();
    }
}
