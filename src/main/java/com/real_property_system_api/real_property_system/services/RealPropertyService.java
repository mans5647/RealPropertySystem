package com.real_property_system_api.real_property_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.real_property_system_api.real_property_system.models.RealProperty;
import com.real_property_system_api.real_property_system.repos.RealPropertyRepository;
import com.real_property_system_api.real_property_system.specs.RealPropertySpecification;

@Service
public class RealPropertyService {

    @Autowired
    private RealPropertyRepository realPropertyRepository;

    public List<RealProperty> findAll() {
        return (List<RealProperty>) realPropertyRepository.findAll();
    }

    public Page<RealProperty> findAllPaged(Pageable pageable) {
        return realPropertyRepository.findAll(pageable);
    }

    public RealProperty findById(Long id) {
        return realPropertyRepository.findById(id).orElse(null);
    }

    public boolean existsByTitle(String title)
    {
        return realPropertyRepository.findByPropTitle(title).isPresent();
    }

    public boolean existsById(Long id)
    {
        return realPropertyRepository.findById(id).isPresent();
    }

    public RealProperty save(RealProperty realProperty) {
        return realPropertyRepository.save(realProperty);
    }

    @Transactional
    public void deleteById(Long id) {
        realPropertyRepository.deleteById(id);
    }

    public List<RealProperty> filterProperties(Double minPrice, Double maxPrice, Long ownerId, Double minArea) {
        Specification<RealProperty> spec = Specification
                .where(minPrice != null && maxPrice != null ? RealPropertySpecification.hasPriceBetween(minPrice, maxPrice) : null)
                .and(ownerId != null ? RealPropertySpecification.hasOwnerId(ownerId) : null)
                .and(minArea != null ? RealPropertySpecification.hasAreaGreaterThan(minArea) : null);

        return realPropertyRepository.findAll(spec);
    }
}
