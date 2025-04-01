package com.real_property_system_api.real_property_system.specs;

import org.springframework.data.jpa.domain.Specification;

import com.real_property_system_api.real_property_system.models.RealProperty;

public class RealPropertySpecification {

    public static Specification<RealProperty> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    public static Specification<RealProperty> hasOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<RealProperty> hasAreaGreaterThan(Double area) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("area"), area);
    }
}
