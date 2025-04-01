package com.real_property_system_api.real_property_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.SoilType;
import com.real_property_system_api.real_property_system.repos.SoilTypeRepository;

import java.util.List;

@Service
public class SoilTypeService {

    @Autowired
    private SoilTypeRepository soilTypeRepository;

    public List<SoilType> findAll() {
        return (List<SoilType>) soilTypeRepository.findAll();
    }

    public Page<SoilType> findAllPaged(Pageable pageable) {
        return soilTypeRepository.findAll(pageable);
    }

    public SoilType findById(Long id) {
        return soilTypeRepository.findById(id).orElse(null);
    }

    public SoilType save(SoilType soilType) {
        return soilTypeRepository.save(soilType);
    }

    public void deleteById(Long id) {
        soilTypeRepository.deleteById(id);
    }
}
