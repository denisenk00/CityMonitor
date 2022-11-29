package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import org.springframework.data.domain.Page;


import java.util.List;

public interface LayoutService {
    List<Layout> getAllLayouts();
    List<Layout> getNotDeprecatedLayouts();
    Layout getLayoutById(Long id);
    void saveLayout(Layout layout);
    Page<Layout> getPageOfLayouts(int pageNumber, int size);
    void deleteLayout(Long id);
    LayoutStatus markLayoutAsActual(Long layoutId);
    LayoutStatus markLayoutAsDeprecated(Long layoutId);
}
