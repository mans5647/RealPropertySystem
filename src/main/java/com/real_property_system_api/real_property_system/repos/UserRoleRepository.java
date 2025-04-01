package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real_property_system_api.real_property_system.models.UserRole;


public interface UserRoleRepository extends JpaRepository<UserRole, Long>
{
    UserRole findBySuffix(String suffix);
}
