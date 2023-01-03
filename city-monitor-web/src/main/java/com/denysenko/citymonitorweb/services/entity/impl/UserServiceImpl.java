package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.entities.User;
import com.denysenko.citymonitorweb.repositories.hibernate.UserRepository;
import com.denysenko.citymonitorweb.services.entity.UserServicee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServicee {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача з id = " + id));
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача з username = " + username));
    }

    @Override
    public void saveUser(User user){
        userRepository.save(user);
    }

}
