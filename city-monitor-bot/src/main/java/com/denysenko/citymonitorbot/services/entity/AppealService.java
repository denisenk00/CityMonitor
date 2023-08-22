package com.denysenko.citymonitorbot.services.entity;

import com.denysenko.citymonitorbot.models.Appeal;

import javax.validation.constraints.NotNull;

public interface AppealService {

    void saveAppeal(@NotNull Appeal appeal);

}
