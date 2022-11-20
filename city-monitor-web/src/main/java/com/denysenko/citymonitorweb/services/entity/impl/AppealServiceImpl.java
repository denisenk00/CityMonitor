package com.denysenko.citymonitorweb.services.entity.impl;


import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.repositories.hibernate.AppealRepository;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppealServiceImpl implements AppealService {

    @Autowired
    private AppealRepository appealRepository;

    @Override
    public long countOfUnreadAppeals(){
        return appealRepository.countByStatusEquals(AppealStatus.UNREAD);
    }
}
