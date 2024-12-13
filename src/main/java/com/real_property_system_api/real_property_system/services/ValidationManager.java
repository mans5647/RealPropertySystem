package com.real_property_system_api.real_property_system.services;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class ValidationManager 
{
    private static ValidationManager value;

    public static ValidationManager getManager()
    {
        if (value == null)
        {
            value = new ValidationManager();
        }

        return value;
    }

    
}
