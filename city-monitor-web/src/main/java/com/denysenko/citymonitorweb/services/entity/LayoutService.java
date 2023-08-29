package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.LayoutPreviewDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface LayoutService {

    List<LayoutPreviewDTO> getNotDeprecatedLayoutsPreviews();
    LayoutDTO getLayoutDTOById(@NotNull Long id);
    Layout getFullLayoutById(@NotNull Long id);
    void saveLayout(@NotNull Layout layout);
    void saveLayout(@NotNull LayoutDTO layoutDTO);
    Page<LayoutPreviewDTO> getPageOfLayouts(int pageNumber, int size);
    void deleteLayout(@NotNull Long id);
    LayoutStatus markLayoutAsActual(@NotNull Long layoutId);
    LayoutStatus markLayoutAsDeprecated(@NotNull Long layoutId);
    LayoutStatus getStatusById(Long id);
}
