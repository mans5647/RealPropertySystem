package com.real_property_system_api.real_property_system.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.real_property_system_api.real_property_system.CredentialsStorage;
import com.real_property_system_api.real_property_system.services.JwtManager;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig 
{
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public CredentialsStorage credentialsStorage()
    {
        return new CredentialsStorage(secret);
    }

    @Bean 
    public JwtManager jwtManager()
    {
        return new JwtManager();
    }

}
