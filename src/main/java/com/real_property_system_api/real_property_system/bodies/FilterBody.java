package com.real_property_system_api.real_property_system.bodies;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FilterBody 
{
    private String login;
    private String firstname;
    private String lastname;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDateMin;
    
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDateMax;
    private String role;    
}
