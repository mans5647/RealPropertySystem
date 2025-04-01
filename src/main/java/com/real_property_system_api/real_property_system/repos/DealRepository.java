package com.real_property_system_api.real_property_system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.Deal;
import com.real_property_system_api.real_property_system.models.DealStatus;
import com.real_property_system_api.real_property_system.models.DealType;
import com.real_property_system_api.real_property_system.models.RealProperty;
import com.real_property_system_api.real_property_system.models.User;

@Repository
public interface DealRepository extends PagingAndSortingRepository<Deal, Long> 
{
    List<Deal> findAll();
    Optional<Deal> findById(Long id);
    
    Deal save(Deal deal);
    void deleteById(Long id);

    List<Deal> findByDealStatusStatusId(Long statusId);
    
    List<Deal> findByDealTypeTypeId(Long dealTypeId);
    List<Deal> findByDealPriceBetween(Double min, Double max);


    Optional<Deal> findByProperty(RealProperty property);
    List<Deal> findByDealStatus(DealStatus dealStatus);
    List<Deal> findByDealType(DealType dealType);
    List<Deal> findByBuyer(User buyer);

    Page<Deal> findAllByBuyer(Pageable pageable, User buyer);

    @Query(value = "select count(*) from Deal d")
    public Long countAll();
}
