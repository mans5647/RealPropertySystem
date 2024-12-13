package com.real_property_system_api.real_property_system.services;

import java.text.Collator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.real_property_system_api.real_property_system.repos.UserRepository;


public class AuthProvider implements AuthenticationProvider 
{

    @Autowired
    private UserRepository userRepository;

    private DatabaseUserDetails databaseUserDetails;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException 
    {
        var userDetails = databaseUserDetails.loadUserByUsername(authentication.getName());
        boolean matches = authentication.getCredentials().toString().equals(userDetails.getPassword());
        
        if (!matches) throw new BadCredentialsException("Неправильный пароль");

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) 
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    public void setService(DatabaseUserDetails userDetailsService)
    {
        this.databaseUserDetails = userDetailsService;
    }

    public void setEncoder(PasswordEncoder encoder)
    {
        this.passwordEncoder = encoder;
    }

}
