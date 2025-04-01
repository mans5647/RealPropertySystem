package com.real_property_system_api.real_property_system.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.Passport;
import com.real_property_system_api.real_property_system.repos.PassportRepository;


@Service
public class PassportService 
{
    @Autowired
    private PassportRepository repository;


    public Passport Save(Passport value)
    {
        return repository.save(value);
    }


    public Optional<Passport> Update(Passport p)
    {
        var holder = GetById(p.getId());
        if (holder.isPresent())
        {
            return Optional.of(Save(p));
        }

        return Optional.empty();
    }

    public Optional<Passport> GetById(Long id)
    {
        return repository.findById(id);
    }

    public boolean Delete(Passport p)
    {
        repository.delete(p);

        if (GetById(p.getId()).isPresent()) return false;
        
        return true;
    }
}
