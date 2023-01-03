package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface UserServicee {

    User getUserById(@NotNull Long id);
    User getUserByUsername(@NotBlank String username);
    void saveUser(@NotNull User user);
}
