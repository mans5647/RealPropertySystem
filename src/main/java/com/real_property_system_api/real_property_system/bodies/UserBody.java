package com.real_property_system_api.real_property_system.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.real_property_system_api.real_property_system.models.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserBody 
{
    
    private User user;

    @JsonProperty("role_id")
    private Long roleId;

    @JsonProperty("passport_id")
    private Long passportId;

    
}
