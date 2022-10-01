package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.models.entities.Layout;

import java.util.List;

public interface LayoutService {
    List<Layout> getAllLayouts();
    Layout getLayoutById(Long id);
}
