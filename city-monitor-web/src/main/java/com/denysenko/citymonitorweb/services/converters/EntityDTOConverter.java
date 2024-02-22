package com.denysenko.citymonitorweb.services.converters;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Validated
public interface EntityDTOConverter<Entity, DTO> {
    Entity convertDTOToEntity(@NotNull DTO dto) throws ConversionFailedException;
    DTO convertEntityToDTO(@NotNull Entity entity) throws ConversionFailedException;

    default List<Entity> convertListsDTOToEntity(@NotNull List<DTO> dtos) throws ConversionFailedException {
        List<Entity> entities = new LinkedList<>();
        for (DTO dto : dtos) {
            entities.add(convertDTOToEntity(dto));
        }
        return entities;
    }

    default List<DTO> convertListsEntityToDTO(@NotNull List<Entity> entities) throws ConversionFailedException {
        List<DTO> dtos = new LinkedList<>();
        for (Entity entity : entities) {
            dtos.add(convertEntityToDTO(entity));
        }
        return dtos;
    }
}
