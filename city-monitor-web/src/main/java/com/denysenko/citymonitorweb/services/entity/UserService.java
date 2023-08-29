package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import com.denysenko.citymonitorweb.models.dto.UserDTO;
import com.denysenko.citymonitorweb.models.entities.User;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface UserService {

    User getUserById(@NotNull Long id);
    User getUserByUsername(@NotBlank String username);
    UserDTO getUserDTOByUsername(@NotBlank String username);
    void saveUser(@NotNull User user);
    Page<UserDTO> getPageOfUsersDTO(int pageNumber, int size);
    boolean userWithUsernameExists(@NotBlank String username);
    void updateUserAccountStatus(@NotBlank String username, @NotNull UserAccountStatus status);
    void updateUserRole(@NotBlank String username, @NotNull UserRole role);
    String createUser(@NotBlank String username, @NotNull UserRole userRole);
    String resetUserPassword(@NotBlank String username);

}
