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

import com.real_property_system_api.real_property_system.models.LandCategory;
import com.real_property_system_api.real_property_system.services.LandCategoryService;

@RestController
@RequestMapping("/api/resources/land_categories")
public class LandCategoryController 
{
    @Autowired
    private LandCategoryService landCategoryService;

    @GetMapping("/fetch")
    public List<LandCategory> fetchAll()
    {
        return landCategoryService.findAll();
    }


    @GetMapping("/fetch/{id}")
    private LandCategory fetchById(@PathVariable("id") Long id)
    {
        return landCategoryService.findById(id);
    }

    @PostMapping("/add")
    private LandCategory addNewLandCategory(@RequestBody LandCategory value)
    {
        return landCategoryService.save(value);
    }

    @PutMapping("/upd/{id}")
    private LandCategory updateLandCategoryById(@PathVariable("id") Long id, @RequestBody LandCategory value)
    {
        return landCategoryService.save(value);
    }

    @DeleteMapping("/del/{id}")
    private void deleteLandCategory(@PathVariable("id") Long id)
    {
        landCategoryService.deleteById(id);
    }
}
