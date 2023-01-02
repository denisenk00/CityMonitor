package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.entities.Layout;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface LayoutService {
    List<Layout> getNotDeprecatedLayouts();
    Layout getLayoutById(@NotNull Long id);
    void saveLayout(@NotNull Layout layout);
    Page<Layout> getPageOfLayouts(int pageNumber, int size);
    void deleteLayout(@NotNull Long id);
    LayoutStatus markLayoutAsActual(@NotNull Long layoutId);
    LayoutStatus markLayoutAsDeprecated(@NotNull Long layoutId);
}
