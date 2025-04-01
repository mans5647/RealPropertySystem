package com.real_property_system_api.real_property_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.DealStatus;
import com.real_property_system_api.real_property_system.repos.DealStatusRepository;

@Service
public class DealStatusService 
{
    @Autowired
    private DealStatusRepository repository;
    
    
    public DealStatus add(DealStatus value)
    {
        return repository.save(value);
    }

    public List<DealStatus> findAll()
    {
        return repository.findAll();
    }

    public boolean existsByStatusType(Integer i)
    {
        return repository.findByStatusTechType(i).isPresent();
    }

    public DealStatus findByStatus(Integer i)
    {
        return repository.findByStatusTechType(i).get();
    }
}
