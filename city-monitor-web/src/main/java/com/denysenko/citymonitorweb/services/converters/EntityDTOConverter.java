package com.denysenko.citymonitorweb.services.converters;

import org.springframework.core.convert.ConversionFailedException;

import java.util.LinkedList;
import java.util.List;

public interface EntityDTOConverter<Entity, DTO>{
    Entity convertDTOToEntity(DTO dto) throws ConversionFailedException;
    DTO convertEntityToDTO(Entity entity) throws ConversionFailedException;

    default List<Entity> convertListsDTOToEntity(List<DTO> dtos) throws ConversionFailedException{
        List<Entity> entities = new LinkedList<>();
        for(DTO dto : dtos) {
            entities.add(convertDTOToEntity(dto));
        }
        return entities;
    }

    default List<DTO> convertListsEntityToDTO(List<Entity> entities) throws ConversionFailedException{
        List<DTO> dtos = new LinkedList<>();
        for(Entity entity : entities){
            dtos.add(convertEntityToDTO(entity));
        }
        return dtos;
    }
}
