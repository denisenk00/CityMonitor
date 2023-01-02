package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.dto.ResultDTO;
import com.denysenko.citymonitorweb.models.entities.Result;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;


@Service
public class ResultEntityToDTOConverter implements EntityDTOConverter<Result, ResultDTO> {

    @Autowired
    private OptionEntityToDTOConverter optionConverter;

    @Override
    public Result convertDTOToEntity(ResultDTO resultDTO){
        throw new UnsupportedOperationException("Метод convertDTOToEntity(ResultDTO resultDTO) не реалізовано.");
    }

    @Override
    public ResultDTO convertEntityToDTO(Result result) {
        try {
            OptionDTO optionDTO = optionConverter.convertEntityToDTO(result.getOption());
            return ResultDTO.builder()
                    .id(result.getId())
                    .option(optionDTO)
                    .polygonId(result.getPolygon().getId())
                    .answersCnt(result.getAnswersCount())
                    .build();
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(result), TypeDescriptor.valueOf(ResultDTO.class), null, e);
        }
    }
}
