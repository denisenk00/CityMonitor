package com.denysenko.citymonitorweb.services.impl;


import com.denysenko.citymonitorweb.repositories.hibernate.UserRepository;
import com.denysenko.citymonitorweb.services.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


}