package com.real_property_system_api.real_property_system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.LandCategory;
import com.real_property_system_api.real_property_system.models.LandCategory;

@Repository
public interface LandCategoryRepository extends PagingAndSortingRepository<LandCategory, Long>
{
    Optional<LandCategory> findById(Long id);

    List<LandCategory> findAll();
    LandCategory save(LandCategory value);
    void deleteById(Long id);

}
