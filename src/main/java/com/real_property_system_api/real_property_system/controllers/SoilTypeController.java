package com.real_property_system_api.real_property_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;

import com.real_property_system_api.real_property_system.models.SoilType;
import com.real_property_system_api.real_property_system.services.SoilTypeService;


@RestController
@RequestMapping("/api/resources/soils")
public class SoilTypeController 
{

    @Autowired
    private SoilTypeService soilTypeService;

    @GetMapping("/fetch")
    public List<SoilType> fetchAll()
    {
        return soilTypeService.findAll();
    }


    @GetMapping("/fetch/{id}")
    private SoilType fetchById(@PathVariable("id") Long id)
    {
        return soilTypeService.findById(id);
    }

    @PostMapping("/add")
    private SoilType addNewSoilType(@RequestBody SoilType value)
    {
        return soilTypeService.save(value);
    }

    @PutMapping("/upd/{id}")
    private SoilType updateSoilTypeById(@PathVariable("id") Long id, @RequestBody SoilType value)
    {
        return soilTypeService.save(value);
    }

    @DeleteMapping("/del/{id}")
    private void deleteSoilType(@PathVariable("id") Long id)
    {
        soilTypeService.deleteById(id);
    }
}
