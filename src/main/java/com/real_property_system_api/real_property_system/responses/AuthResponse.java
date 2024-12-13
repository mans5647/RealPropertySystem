package com.real_property_system_api.real_property_system.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse 
{
    @JsonProperty("code")
    public int authCode;

    @JsonProperty("message")
    public String message;
    
}
