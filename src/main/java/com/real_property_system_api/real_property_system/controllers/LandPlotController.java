package com.real_property_system_api.real_property_system.controllers;

import java.text.Collator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.bodies.LandPlotRequestBody;
import com.real_property_system_api.real_property_system.bodies.PropertyLandPlotBody;
import com.real_property_system_api.real_property_system.models.LandCategory;
import com.real_property_system_api.real_property_system.models.LandPlot;
import com.real_property_system_api.real_property_system.models.RealProperty;
import com.real_property_system_api.real_property_system.models.SoilType;
import com.real_property_system_api.real_property_system.services.LandCategoryService;
import com.real_property_system_api.real_property_system.services.LandPlotService;
import com.real_property_system_api.real_property_system.services.RealPropertyService;
import com.real_property_system_api.real_property_system.services.SoilTypeService;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

@RestController
@RequestMapping("/api/resources/land_plots")
public class LandPlotController 
{
    
    @Autowired
    private LandPlotService landPlotService;

    @Autowired
    private RealPropertyService realPropertyService;

    @Autowired
    private SoilTypeService soilTypeService;

    @Autowired
    private LandCategoryService landCategoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewLandPlot(@RequestBody LandPlot value)
    {
        landPlotService.save(value);   
        return ResponseEntity.ok()
            .build();
    }

    @PutMapping("/upd/{id}")
    public ResponseEntity<LandPlot> updateLandPlot(@PathVariable("id") Long id, @RequestBody LandPlot value)
    {
        var h = landPlotService.findById(id);
        if (h != null)
        {
            final var savedInstance = landPlotService.save(value);
            return ResponseEntity.ok()
            .body(savedInstance);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/fetch_by_property_id/{id}")
    public ResponseEntity<LandPlot> fetchByPropertyId(@PathVariable("id") Long propId)
    {
        if (landPlotService.doesRecordWithPropertyIdExists(propId))
        {
            return ResponseEntity.ok()
                .body(landPlotService.findByPropertyId(propId).get());
        }

        return ResponseEntity.notFound()
            .build();
    }

    @DeleteMapping("/delete_if_prev_diffs/{id}")
    private ResponseEntity<String> deleteByPropertyId(@PathVariable("id") Long propertyId)
    {
        var property = realPropertyService.findById(propertyId);
        
        if (property != null)
        {
            var deletable = landPlotService.findByPropertyId(propertyId);

            if (deletable.isPresent())
            {
                landPlotService.deleteById(deletable.get().getPlotId());
                return ResponseEntity.ok().build();
            }
            

        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete_rel/{property_id}")
    private ResponseEntity<String> deleteIfLandAndPlotConnected(@PathVariable("property_id") Long propertyId)
    {
        var valueHolder = landPlotService.findByPropertyId(propertyId);
        
        if (valueHolder.isPresent())
        {
            var valueToBeDeleted = valueHolder.get();

            landPlotService.deleteById(valueToBeDeleted.getPlotId());

            return ResponseEntity.ok()
                .build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{self_id}")
    private ResponseEntity<String> deleteSelfById(@PathVariable("self_id") Long id)
    {
        var value = landPlotService.findById(id);
        
        if (value != null)
        {

            landPlotService.deleteById(value.getPlotId());

            return ResponseEntity.ok()
                .build();
        }

        return ResponseEntity.notFound().build();
    }
}
