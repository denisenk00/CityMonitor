package com.denysenko.citymonitorweb.services;

import java.util.LinkedList;
import java.util.List;

public interface EntityDTOConverter<Entity, DTO>{
    Entity convertDTOToEntity(DTO dto) throws Exception;
    DTO convertEntityToDTO(Entity entity) throws Exception;

    default List<Entity> convertListsDTOToEntity(List<DTO> dtos) throws Exception{
        List<Entity> entities = new LinkedList<>();
        for(DTO dto : dtos) {
            entities.add(convertDTOToEntity(dto));
        }
        return entities;
    }

    default List<DTO> convertListsEntityToDTO(List<Entity> entities) throws Exception{
        List<DTO> dtos = new LinkedList<>();
        for(Entity entity : entities){
            dtos.add(convertEntityToDTO(entity));
        }
        return dtos;
    }
}
