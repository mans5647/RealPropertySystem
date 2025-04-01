package com.real_property_system_api.real_property_system.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericMessage
{

    @JsonProperty("code")
    public int  code;

    @JsonProperty("message")
    public String message;
}
