package com.real_property_system_api.real_property_system;

import org.springframework.stereotype.Component;

@Component
public class CredentialsStorage 
{
    private final String jwtSecret;
    public CredentialsStorage(String jwtSecret)
    {
        this.jwtSecret = jwtSecret;

    }
    
    public String getSecret()
    {
        return jwtSecret;
    }
}
