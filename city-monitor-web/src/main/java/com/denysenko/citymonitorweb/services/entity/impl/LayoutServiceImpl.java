package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.repositories.hibernate.LayoutRepository;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<Layout> getPageOfLayouts(int pageNumber, int size) {
        if(pageNumber < 1 || size < 1) throw new IllegalArgumentException();

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Layout> layoutPage = layoutRepository.findAll(request);
        return layoutPage;
    }

    @Override
    public List<Layout> getNotDeprecatedLayouts() {
        return layoutRepository.findByStatusNot(LayoutStatus.DEPRECATED);
    }
}
