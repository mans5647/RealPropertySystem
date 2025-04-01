package com.real_property_system_api.real_property_system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.DealType;

@Repository
public interface DealTypeRepository extends PagingAndSortingRepository<DealType, Long> 
{
    public DealType save(DealType value);
    public DealType findById(Long id);
    public List<DealType> findAll();
    public Optional<DealType> findByTypeTech(Integer typeTech);

}
