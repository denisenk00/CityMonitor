package com.denysenko.citymonitorbot.services;

import com.denysenko.citymonitorbot.models.entities.Appeal;
import com.denysenko.citymonitorbot.repositories.hibernate.AppealDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppealService {
    @Autowired
    private AppealDAO appealDAO;

    public void saveAppeal(Appeal appeal){
        appealDAO.save(appeal);
    }
}
