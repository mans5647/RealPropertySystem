package com.real_property_system_api.real_property_system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.real_property_system_api.real_property_system.models.LandPlot;
import com.real_property_system_api.real_property_system.repos.LandPlotRepository;

@Service
public class LandPlotService {

    
    @Autowired
    private LandPlotRepository landPlotRepository;

    public List<LandPlot> findAll() {
        return (List<LandPlot>) landPlotRepository.findAll();
    }

    public Page<LandPlot> findAllPaged(Pageable pageable) {
        return landPlotRepository.findAll(pageable);
    }

    public LandPlot findById(Long id) {
        return landPlotRepository.findById(id).orElse(null);
    }

    public List<LandPlot> findByAreaRange(Double minArea, Double maxArea) {
        return landPlotRepository.findByPropertyPropAreaBetween(minArea, maxArea);
    }

    public List<LandPlot> findBySoilType(Long soilTypeId) {
        return landPlotRepository.findBySoilTypeSoilId(soilTypeId);
    }

    public LandPlot save(LandPlot landPlot) {
        return landPlotRepository.save(landPlot);
    }

    @Transactional
    public void deleteById(Long id) 
    {
        landPlotRepository.deleteById(id);
    }

    public Optional<LandPlot> findByPropertyId(Long propId)
    {
        return landPlotRepository.findByPropertyPropId(propId);
    }

    public boolean doesRecordWithPropertyIdExists(Long propId)
    {
        return findByPropertyId(propId).isPresent();
    }
}
