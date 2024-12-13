package com.real_property_system_api.real_property_system.bodies;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;


@Validated
public class RegisterBodyWithRole 
{

    @JsonProperty("login")
    @NotBlank
    @Size(min = 8, message = "Логин должен иметь минимум 8 символов")
    @NotNull
    private String login;

    @JsonProperty("password")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    
    @NotNull
    private String password;
    
    @JsonProperty("role")
    @NotNull(message = "Роль не должна быть пустой")
    private String RoleSuffix;

    public RegisterBodyWithRole()
    {
        
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleSuffix() {
        return RoleSuffix;
    }

    public void setRoleSuffix(String roleSuffix) {
        RoleSuffix = roleSuffix;
    }


    
}
