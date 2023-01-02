package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.dto.FileDTO;
import com.denysenko.citymonitorweb.models.dto.LocalDTO;
import com.denysenko.citymonitorweb.models.dto.LocationPointDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AppealEntityToDTOConverter implements EntityDTOConverter<Appeal, AppealDTO> {
    @Autowired
    private FileEntityToDTOConverter fileEntityToDTOConverter;
    @Autowired
    private LocalEntityToDTOConverter localEntityToDTOConverter;

    @Override
    public Appeal convertDTOToEntity(AppealDTO appealDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AppealDTO convertEntityToDTO(Appeal appeal) throws ConversionFailedException {
        try {
            List<FileDTO> fileDTOs = fileEntityToDTOConverter.convertListsEntityToDTO(appeal.getFiles());
            Point point = appeal.getLocationPoint();
            LocationPointDTO locationPointDTO = null;
            if (Objects.nonNull(point)) {
                locationPointDTO = new LocationPointDTO(point.getY(), point.getX());
            }
            LocalDTO localDTO = localEntityToDTOConverter.convertEntityToDTO(appeal.getLocal());
            return AppealDTO.builder()
                    .id(appeal.getId())
                    .text(appeal.getText())
                    .postDate(appeal.getPostDate())
                    .fileDTOs(fileDTOs)
                    .locationPointDTO(locationPointDTO)
                    .status(appeal.getStatus().getTitle())
                    .local(localDTO)
                    .build();
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(appeal), TypeDescriptor.valueOf(AppealDTO.class), null, e);
        }
    }
}
