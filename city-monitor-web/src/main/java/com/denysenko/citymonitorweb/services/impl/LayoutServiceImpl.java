package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.repositories.hibernate.LayoutRepository;
import com.denysenko.citymonitorweb.services.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LayoutServiceImpl implements LayoutService {
    @Autowired
    private LayoutRepository layoutRepository;
    @Override
    public List<Layout> getAllLayouts() {
        return layoutRepository.findAll();
    }

    public Layout getLayoutById(Long id){
        return layoutRepository.getById(id);
    }
}
