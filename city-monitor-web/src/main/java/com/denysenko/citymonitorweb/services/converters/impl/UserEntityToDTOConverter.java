package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import com.denysenko.citymonitorweb.models.dto.UserDTO;
import com.denysenko.citymonitorweb.models.entities.User;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

@Service
public class UserEntityToDTOConverter implements EntityDTOConverter<User, UserDTO> {
    @Override
    public User convertDTOToEntity(UserDTO userDTO) throws ConversionFailedException {
        try {
            User user = new User();
            user.setId(userDTO.getId());
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setRole(UserRole.valueOf(userDTO.getRole()));
            user.setUserAccountStatus(UserAccountStatus.valueOf(userDTO.getUserAccountStatus()));
            return user;
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(userDTO), TypeDescriptor.valueOf(User.class), null, e);
        }
    }

    @Override
    public UserDTO convertEntityToDTO(User user) throws ConversionFailedException {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole().name());
            userDTO.setUserAccountStatus(user.getUserAccountStatus().name());
            return userDTO;
        }catch (Exception e){
            throw new ConversionFailedException(TypeDescriptor.forObject(user), TypeDescriptor.valueOf(UserDTO.class), null, e);
        }
    }
}
