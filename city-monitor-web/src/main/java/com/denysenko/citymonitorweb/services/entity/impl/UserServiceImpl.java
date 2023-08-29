package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.dto.UserDTO;
import com.denysenko.citymonitorweb.models.entities.User;
import com.denysenko.citymonitorweb.repositories.hibernate.UserRepository;
import com.denysenko.citymonitorweb.services.entity.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача з id = " + id));
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача з username = " + username));
    }

    @Override
    public UserDTO getUserDTOByUsername(String username) {
        return userRepository.findDTOByUsername(username).orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача з username = " + username));
    }

    @Override
    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> getPageOfUsersDTO(int pageNumber, int size) {
        if (pageNumber < 1 || size < 1)
            throw new IllegalArgumentException("Номер сторінки та кількість елементів мають бути більшими за нуль.");
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "username"));
        Page<UserDTO> usersPage = userRepository.findAllUserDTOBy(request);
        return usersPage;
    }

    public boolean userWithUsernameExists(String username){
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void updateUserAccountStatus(String username, UserAccountStatus status){
        User user = getUserByUsername(username);
        user.setUserAccountStatus(status);
    }
    @Transactional
    public void updateUserRole(String username, UserRole role){
        User user = getUserByUsername(username);
        user.setRole(role);
    }

    @Transactional
    public String createUser(String username, UserRole userRole){
        String generatedPassword = RandomStringUtils.randomAlphanumeric(15);
        String encodedPassword = passwordEncoder.encode(generatedPassword);
        User user = new User(username, encodedPassword, userRole);
        userRepository.save(user);
        return generatedPassword;
    }

    @Transactional
    public String resetUserPassword(String username){
        String generatedPassword = RandomStringUtils.randomAlphanumeric(15);
        String encodedPassword = passwordEncoder.encode(generatedPassword);
        User user = getUserByUsername(username);
        user.setPassword(encodedPassword);
        return generatedPassword;
    }
}
