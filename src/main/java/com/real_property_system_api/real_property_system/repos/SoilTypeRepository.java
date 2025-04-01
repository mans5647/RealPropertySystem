package com.real_property_system_api.real_property_system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.SoilType;

@Repository
public interface SoilTypeRepository extends PagingAndSortingRepository<SoilType, Long> {
    // Add default CRUD and sorting support

    List<SoilType> findAll();


    Optional<SoilType> findById(Long id);

    SoilType save(SoilType soilType);
    void deleteById(Long id);

}
