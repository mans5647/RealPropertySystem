package com.real_property_system_api.real_property_system.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.real_property_system_api.real_property_system.JwtFilter;
import com.real_property_system_api.real_property_system.services.AuthProvider;
import com.real_property_system_api.real_property_system.services.DatabaseUserDetails;
import com.real_property_system_api.real_property_system.services.JwtManager;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
            .authorizeHttpRequests(x ->
            x
            .requestMatchers(HttpMethod.POST,"/api/public/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
            .requestMatchers(HttpMethod.GET,"/api/resources/**").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
            
            );

        http.exceptionHandling(x -> 
        x.authenticationEntryPoint((res, re, q) ->
        {
            re.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        ));

        http.addFilterAt(new JwtFilter(jwtManager, authenticationManager()), AuthorizationFilter.class);
        http.csrf(x ->
        x.disable());

        return http.build();
    }

    
    @Bean 
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public DatabaseUserDetails userDetailsService()
    {
        return new DatabaseUserDetails();
    }

    @Bean 
    public AuthenticationManager authenticationManager()
    {
        AuthProvider authProvider = new AuthProvider();
        authProvider.setEncoder(passwordEncoder());
        authProvider.setService(userDetailsService());
        return new ProviderManager(authProvider);
    }


    @Autowired
    private JwtManager jwtManager;


    
    public JwtFilter jwtFilter()
    {
        return new JwtFilter(jwtManager, authenticationManager());
    }



}
