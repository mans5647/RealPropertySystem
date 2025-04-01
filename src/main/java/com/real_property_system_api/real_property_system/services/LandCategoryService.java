package com.real_property_system_api.real_property_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.LandCategory;
import com.real_property_system_api.real_property_system.models.LandCategory;
import com.real_property_system_api.real_property_system.repos.LandCategoryRepository;

@Service
public class LandCategoryService 
{

    @Autowired
    private LandCategoryRepository repository;

    public LandCategory findById(Long id)
    {
        return repository.findById(id).orElse(null);
    }

    public Page<LandCategory> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<LandCategory> findAll()
    {
        return repository.findAll();
    }

    public LandCategory save(LandCategory value) {
        return repository.save(value);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
