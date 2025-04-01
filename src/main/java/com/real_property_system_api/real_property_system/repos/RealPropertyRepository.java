package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.Apartment;
import com.real_property_system_api.real_property_system.models.LandPlot;
import com.real_property_system_api.real_property_system.models.RealProperty;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealPropertyRepository extends PagingAndSortingRepository<RealProperty, Long> {
    // Find all properties with sorting
    List<RealProperty> findAll();
    Optional<RealProperty> findById(Long id);
    // Add custom query methods for filtering if necessary
    List<RealProperty> findByPropPriceBetween(Double minPrice, Double maxPrice);

    Optional<RealProperty> findByPropTitle(String title);
    

    RealProperty save(RealProperty realProperty);
    void deleteById(Long id);

    List<RealProperty> findAll(Specification<RealProperty> spec);

}
