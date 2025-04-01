package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.RealProperty;
import com.real_property_system_api.real_property_system.models.RealPropertyDealType;

@Repository
public interface RealPropertyDTRepo extends PagingAndSortingRepository<RealPropertyDealType, Long>
{
    RealPropertyDealType save(RealPropertyDealType value);
    RealPropertyDealType findByRecordId(Long id);
    RealPropertyDealType findByProperty(RealProperty property);
}
