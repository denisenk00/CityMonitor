package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.repositories.hibernate.LayoutRepository;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
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
        if(id == null) throw new IllegalArgumentException("Id should be not NULL");
        return layoutRepository.getById(id);
    }

    public void saveLayout(Layout layout) {
          if(layout == null) throw new NullPointerException("Layout should be not NULL");
          layoutRepository.save(layout);
    }

}
