package com.real_property_system_api.real_property_system.controllers;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.repos.UserRepository;

@RestController
@RequestMapping("/api/resources")
public class UserController 
{
    @Autowired
    private UserRepository repository;

    @GetMapping("/users/all")
    public List<User> getAllUsers()
    {
        return repository.findAll();
    }


}
