package com.real_property_system_api.real_property_system.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SingleValue<T> 
{
    @JsonProperty("value")
    private T value;

    private SingleValue(T i)
    {
        value = i;
    }

    public static <T> SingleValue<T> of(T value)
    {
        return new SingleValue<T>(value);
    }
}
