package com.real_property_system_api.real_property_system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.DealStatus;

@Repository
public interface DealStatusRepository extends PagingAndSortingRepository<DealStatus, Long> 
{
    List<DealStatus> findAll();
    DealStatus save(DealStatus value);
    void deleteByStatusId(Long id);
    
    Optional<DealStatus> findByStatusTechType(Integer techType);

}
