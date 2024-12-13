package com.real_property_system_api.real_property_system.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse 
{
    @JsonProperty("error")
    private int error;

    @JsonProperty("access")
    private String access_token;
    
    @JsonProperty("refresh")
    private String refresh_token;

    public JwtResponse(int error, String access_token, String refresh_token) {
        this.error = error;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
    
    

}
