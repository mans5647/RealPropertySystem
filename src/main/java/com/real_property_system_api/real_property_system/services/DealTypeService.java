package com.real_property_system_api.real_property_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.DealType;
import com.real_property_system_api.real_property_system.repos.DealTypeRepository;


@Service
public class DealTypeService 
{
    @Autowired
    private DealTypeRepository repository;

    public DealType find(Long id)
    {
        return repository.findById(id);
    }

    public List<DealType> findAll()
    {
        return repository.findAll();
    }

    public DealType save(DealType value)
    {
        return repository.save(value);
    }

    public boolean existsByTechType(Integer techType)
    {
        return repository.findByTypeTech(techType).isPresent();
    }

    public DealType findByTechType(Integer techType)
    {
        return repository.findByTypeTech(techType).get();
    }

    public boolean saveIfNotExist(DealType value)
    {
        var h = repository.findByTypeTech(value.getTypeTech());
        
        if (h.isEmpty())
        {
            repository.save(value);
            return true;
        }

        return false;
    }


}
