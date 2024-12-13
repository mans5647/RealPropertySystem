package com.real_property_system_api.real_property_system.models;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.springframework.cglib.core.Local;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rps_users")
public class User implements UserDetails
{
    @Id
    @GeneratedValue
    @Column(name = "uid")
    private Long id;

    @Column(name = "u_firstname", nullable = true)
    private String firstName;
    @Column(name = "u_lastname", nullable = true)
    private String lastName;
    @Column(name = "u_birthdate", nullable = true)
    private LocalDate birthDate;
    @Column(name = "u_login", nullable = false)
    private String login;
    @Column(name = "u_password", nullable = false)
    private String password;


    
    @JoinColumn(name = "u_passport", nullable = true)
    @OneToOne
    private Passport passport;

    @JoinColumn(name = "u_rps_role", nullable = true)
    @ManyToOne
    private UserRole userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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
