package com.real_property_system_api.real_property_system.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.repos.UserRepository;

@Service
public class UserService 
{
    @Autowired
    private UserRepository userRepository;

    public UserService()
    {

    }

    public Optional<User> getUserByLogin(String login)
    {
        return userRepository.findByLogin(login);
    }

    public boolean isUserExists(String login)
    {
        return userRepository.findByLogin(login).isPresent();
    }

    public User CreateNewUser(User user)
    {
        return userRepository.save(user);
    }
}
