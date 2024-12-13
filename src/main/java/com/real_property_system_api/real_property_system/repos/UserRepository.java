package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.real_property_system_api.real_property_system.models.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByLogin(String login);
}