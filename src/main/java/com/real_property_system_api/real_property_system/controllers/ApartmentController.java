package com.real_property_system_api.real_property_system.controllers;

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

import com.real_property_system_api.real_property_system.models.Apartment;
import com.real_property_system_api.real_property_system.services.ApartmentService;
import com.real_property_system_api.real_property_system.services.RealPropertyService;

@RestController
@RequestMapping("/api/resources/apartments")
public class ApartmentController 
{
    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private RealPropertyService realPropertyService;

    

    @PostMapping("/add")
    public ResponseEntity<String> addNewApartment(@RequestBody Apartment value)
    {
        apartmentService.save(value);   
        return ResponseEntity.ok()
            .build();
    }

    @PutMapping("/upd/{id}")
    public ResponseEntity<Apartment> updateApartment(@PathVariable("id") Long id, @RequestBody Apartment value)
    {

        var h = apartmentService.findById(id);

        if (h != null)
        {
            final var savedInstance = apartmentService.save(value);
            return ResponseEntity.ok()
            .body(savedInstance);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/fetch_by_property_id/{id}")
    public ResponseEntity<Apartment> fetchByPropertyId(@PathVariable("id") Long propId)
    {
        if (apartmentService.doesRecordWithPropertyIdExists(propId))
        {
            return ResponseEntity.ok()
                .body(apartmentService.findByPropertyId(propId).get());
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
            var deleteValue = apartmentService.findByPropertyId(propertyId);
            
            if (deleteValue.isPresent())
            {
                apartmentService.deleteById(deleteValue.get().getApartmentId());
                return ResponseEntity.ok()
                .build();

            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete_rel/{property_id}")
    private ResponseEntity<String> deleteIfLandAndPlotConnected(@PathVariable("property_id") Long propertyId)
    {
        var valueHolder = apartmentService.findByPropertyId(propertyId);
        
        if (valueHolder.isPresent())
        {
            var valueToBeDeleted = valueHolder.get();

            apartmentService.deleteById(valueToBeDeleted.getApartmentId());

            return ResponseEntity.ok()
                .build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{self_id}")
    private ResponseEntity<String> deleteSelfById(@PathVariable("self_id") Long id)
    {
        var value = apartmentService.findById(id);
        
        if (value != null)
        {

            apartmentService.deleteById(value.getApartmentId());

            return ResponseEntity.ok()
                .build();
        }

        return ResponseEntity.notFound().build();
    }
}
