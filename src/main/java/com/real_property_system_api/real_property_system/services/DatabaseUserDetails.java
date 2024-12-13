package com.real_property_system_api.real_property_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.real_property_system_api.real_property_system.repos.UserRepository;

public class DatabaseUserDetails implements UserDetailsService 
{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException 
    {
        var user_container = userRepository.findByLogin(login);
        
        if (!user_container.isPresent()) throw new UsernameNotFoundException("Пользователь не найден");

        var userValue = user_container.get();

        return User.builder()
            .username(userValue.getUsername())
            .password(userValue.getPassword())
            .authorities(userValue.getAuthorities()).build();
    }
        
}
