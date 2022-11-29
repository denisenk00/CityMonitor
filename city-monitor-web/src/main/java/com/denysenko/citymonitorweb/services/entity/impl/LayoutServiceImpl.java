package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.repositories.hibernate.LayoutRepository;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LayoutServiceImpl implements LayoutService {
    @Autowired
    private LayoutRepository layoutRepository;
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Layout> getAllLayouts() {
        return layoutRepository.findAll();
    }

    public Layout getLayoutById(Long id){
        if(id == null) throw new IllegalArgumentException("Id should be not NULL");
        return layoutRepository.findById(id).orElseThrow(()-> new NoSuchElementException(""));
    }

    public void saveLayout(Layout layout) {
          if(layout == null) throw new IllegalArgumentException("Layout should be not NULL");
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

    @Override
    public void deleteLayout(Long id) {
        if(id == null) throw new IllegalArgumentException();
        //maybe need to check if layout is used by quizzes

        layoutRepository.deleteById(id);
    }

    public LayoutStatus markLayoutAsDeprecated(Long layoutId){
        if(layoutId == null) throw new IllegalArgumentException();
        setLayoutStatusById(layoutId, LayoutStatus.DEPRECATED);
        return LayoutStatus.DEPRECATED;
    }

    public LayoutStatus markLayoutAsActual(Long layoutId){
        if(layoutId == null) throw new IllegalArgumentException();
        LayoutStatus newStatus;
        if(quizRepository.existsByLayoutId(layoutId)){
            setLayoutStatusById(layoutId, LayoutStatus.IN_USE);
            newStatus = LayoutStatus.IN_USE;
        }else {
            setLayoutStatusById(layoutId, LayoutStatus.AVAILABLE);
            newStatus = LayoutStatus.AVAILABLE;
        }
        return newStatus;
    }

    private void setLayoutStatusById(Long id, LayoutStatus layoutStatus) {
        if(id == null) throw new IllegalArgumentException();
        if(layoutStatus == null) throw new IllegalArgumentException();

        Optional<Layout> layoutOptional = layoutRepository.findById(id);
        layoutOptional.ifPresentOrElse((l)->{
            l.setStatus(layoutStatus);
            layoutRepository.save(l);
        }, ()->{
            throw new NoSuchElementException();
        });
    }


}
