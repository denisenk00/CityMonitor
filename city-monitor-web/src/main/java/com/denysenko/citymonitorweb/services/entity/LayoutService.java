package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Layout;


import java.util.List;

public interface LayoutService {
    List<Layout> getAllLayouts();
    Layout getLayoutById(Long id);
    void saveLayout(Layout layout);
}
