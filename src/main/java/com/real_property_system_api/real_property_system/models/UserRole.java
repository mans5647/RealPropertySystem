package com.real_property_system_api.real_property_system.models;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rps_userroles")
public class UserRole implements GrantedAuthority
{
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "rps_rolename_native")
    private String roleName;

    @Column(name = "rps_rolename_suffix")
    private String suffix;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @JsonIgnore
    @Override
    public String getAuthority() 
    {
        return "ROLE_" + suffix;
    }


}
