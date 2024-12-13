package com.real_property_system_api.real_property_system.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrettyError 
{
    @JsonProperty("field")
    private String fieldName;
    
    @JsonProperty("code")
    private String code;

    public PrettyError(String fieldName, String code, String message) {
        this.fieldName = fieldName;
        this.code = code;
        this.message = message;
    }

    @JsonProperty("message")
    private String message;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
