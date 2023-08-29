package com.denysenko.citymonitorweb.models.dto;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String password;

    private UserRole role;

    private UserAccountStatus userAccountStatus;
}
