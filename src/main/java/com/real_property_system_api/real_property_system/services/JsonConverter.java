package com.real_property_system_api.real_property_system.services;

import java.io.IOException;

import org.springframework.data.util.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter 
{
    static final ObjectMapper objectMapper = new ObjectMapper();


    public static synchronized String toJson(Object value) throws IOException
    {
        return objectMapper.writeValueAsString(value); 
    }

    public static Pair<Boolean, String> doSaveConvert(Object value)
    {
        try 
        {
            return Pair.of(true, toJson(value));
        }
        catch (IOException ex)
        {
            return Pair.of(false, "");
        }

    }
}
