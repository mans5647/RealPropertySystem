package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.repository.CrudRepository;

import com.real_property_system_api.real_property_system.models.Passport;

public interface PassportRepository extends CrudRepository<Passport, Long>
{
    
}
