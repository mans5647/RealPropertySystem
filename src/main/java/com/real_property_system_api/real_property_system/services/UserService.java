package com.real_property_system_api.real_property_system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.real_property_system_api.real_property_system.bodies.FilterBody;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.repos.UserRepository;
import com.real_property_system_api.real_property_system.specs.UserSpecification;

@Service
public class UserService 
{
    @Autowired
    private UserRepository userRepository;

    public UserService()
    {

    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User findById(Long uid)
    {
        return userRepository.findById(uid).get();
    }

    public Optional<User> getUserByLogin(String login)
    {
        return userRepository.findByLogin(login);
    }

    public boolean isUserExists(String login)
    {
        return userRepository.findByLogin(login).isPresent();
    }
    public boolean existsById(Long id)
    {
        return userRepository.findById(id).isPresent();
    }

    public void DeleteUserById(Long id)
    {
        userRepository.deleteById(id);
    }

    public User CreateNewUser(User user)
    {
        return userRepository.save(user);
    }

    public User UpdateUser(User user)
    {
        var found = userRepository.findById(user.getId());
        if (found.isPresent())
        {
            return userRepository.save(user);
        }
        return null;
    }


    public Page<User> GetPageable(Pageable pageable)
    {
        return userRepository.findAll(pageable);
    }

    public Long GetCount()
    {
        return userRepository.GetCount();
    }

    public Page<User> GetFiltered(Pageable pageable, FilterBody filterBody)
    {
        Specification<User> spec = UserSpecification.filterBy(filterBody);

        Page<User> users = userRepository.findAll(spec, pageable);

        return users;
    }
}
