package com.real_property_system_api.real_property_system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.real_property_system_api.real_property_system.models.LandPlot;

@Repository
public interface LandPlotRepository extends PagingAndSortingRepository<LandPlot, Long> 
{
    List<LandPlot> findAll();


    Optional<LandPlot> findById(Long id);

    LandPlot save(LandPlot apartment);

    void deleteById(Long id);

    List<LandPlot> findByPropertyPropAreaBetween(Double min, Double max);

    // Custom query for filtering by soil type
    List<LandPlot> findBySoilTypeSoilId(Long soilTypeId);

    // получить запись по ID недвижиомсти
    Optional<LandPlot> findByPropertyPropId(Long id);

    
}
