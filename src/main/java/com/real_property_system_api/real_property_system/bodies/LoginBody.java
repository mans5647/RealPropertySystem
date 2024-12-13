package com.real_property_system_api.real_property_system.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginBody 
{
    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;


    public LoginBody()
    {

    }


    public String getLogin()
    {
        return login;
    }

    public String getPassword()
    {
        return password;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    

}
