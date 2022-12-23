package com.denysenko.citymonitorweb.services.entity.impl;


import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.repositories.hibernate.AppealRepository;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class AppealServiceImpl implements AppealService {

    @Autowired
    private AppealRepository appealRepository;

    @Override
    public long countOfUnreadAppeals(){
        return appealRepository.countByStatusEquals(AppealStatus.UNREAD);
    }

    @Override
    public Page<Appeal> getPageByStatuses(int pageNumber, int size, Set<AppealStatus> statuses){
        if(pageNumber < 1 || size < 1 || statuses == null || statuses.isEmpty()) throw new IllegalArgumentException();

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "postDate"));
        Page<Appeal> appealsPage = appealRepository.findAllByStatusIn(statuses, request);
        return appealsPage;
    }

    @Transactional
    @Override
    public void updateStatusById(Long id, AppealStatus appealStatus){
        Optional<Appeal> appeal = appealRepository.findById(id);
        appeal.ifPresentOrElse((a)->{
            a.setStatus(appealStatus);
            appealRepository.save(a);
        }, () -> new RuntimeException());
    }
}
