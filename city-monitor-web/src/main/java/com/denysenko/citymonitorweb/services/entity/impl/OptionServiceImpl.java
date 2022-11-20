package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.dto.OptionDTO;
import com.denysenko.citymonitorweb.models.entities.Option;
import com.denysenko.citymonitorweb.repositories.hibernate.OptionRepository;
import com.denysenko.citymonitorweb.services.entity.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    private OptionRepository optionRepository;



}
