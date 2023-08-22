package com.denysenko.citymonitorbot.services.entity.impl;

import com.denysenko.citymonitorbot.models.Appeal;
import com.denysenko.citymonitorbot.repositories.hibernate.AppealRepository;
import com.denysenko.citymonitorbot.services.entity.AppealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;

    @Transactional
    public void saveAppeal(@NotNull Appeal appeal){
        appealRepository.save(appeal);
    }

}
