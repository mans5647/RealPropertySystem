package com.real_property_system_api.real_property_system.models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByPosition;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rps_users")
@Getter
@Setter
public class User implements UserDetails
{
    @Id
    @GeneratedValue
    @Column(name = "uid")
    private Long id;

    @CsvBindByPosition(position = 0)
    @Column(name = "u_firstname", nullable = true)
    private String firstName;

    @CsvBindByPosition(position = 1)
    @Column(name = "u_lastname", nullable = true)
    private String lastName;


    @CsvBindByPosition(position = 4)
    @Column(name = "u_birthdate", nullable = true)
    private LocalDate birthDate;


    @CsvBindByPosition(position = 2)
    @Column(name = "u_login", nullable = false)
    private String login;

    @CsvBindByPosition(position = 3)
    @Column(name = "u_password", nullable = false)
    private String password;


    @CsvBindByPosition(position = 5)
    @JsonProperty("passport")
    @JoinColumn(name = "u_passport", nullable = true)
    @OneToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Passport passport;


    @CsvBindByPosition(position = 6)
    @JoinColumn(name = "u_rps_role", nullable = true)
    @ManyToOne
    private UserRole userRole;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        return Collections.singletonList(getUserRole());
    }

    @Override
    public String getUsername() {
        return login;
    }
    
}
