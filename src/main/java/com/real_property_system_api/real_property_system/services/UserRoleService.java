package com.real_property_system_api.real_property_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.UserRole;
import com.real_property_system_api.real_property_system.repos.UserRoleRepository;

@Service
public class UserRoleService 
{
    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole addNewUserRole(UserRole userRole)
    {
        return userRoleRepository.save(userRole);
    }

    public boolean IsRoleExists(String suffix)
    {
        return userRoleRepository.findBySuffix(suffix) != null;
    }

    public boolean IsRoleExists(Long id)
    {
        return userRoleRepository.findById(id).isPresent();
    }

    public UserRole getById(Long id)
    {
        return userRoleRepository.findById(id).orElse(null);
    }

    public UserRole getBySuffix(String suffix)
    {
        return userRoleRepository.findBySuffix(suffix);
    }


    public UserRole addOrReturn(String suffix)
    {
        if (IsRoleExists(suffix))
        {
            return getBySuffix(suffix);
        }

        UserRole userRole = new UserRole();
        userRole.setSuffix(suffix);
        switch (suffix)
        {
            case "CLIENT":
            {
                userRole.setRoleName("Клиент");
                break;
            }
            case "ADMIN":
            {
                userRole.setRoleName("Администратор");
                break;
            }
            case "MANAGER":
            {
                userRole.setRoleName("Менеджер");
                break;
            }
            case "REALTOR":
            {
                userRole.setRoleName("Риелтор");
                break;
            }
            
        }

        return addNewUserRole(userRole);
    }
}
