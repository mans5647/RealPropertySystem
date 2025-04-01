package com.real_property_system_api.real_property_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.models.RealPropertyDealType;
import com.real_property_system_api.real_property_system.repos.RealPropertyDTRepo;
import com.real_property_system_api.real_property_system.services.RealPropertyService;

@RestController
@RequestMapping("/api/business/prop_to_dealtype")
public class AssocDealWithPropController 
{

    @Autowired
    private RealPropertyDTRepo realPropertyDTRepo;

    @Autowired
    private RealPropertyService realPropertyService;

    @GetMapping("/get_by_property/{id}")
    public RealPropertyDealType fetchByPropertyId(@PathVariable("id") Long propId)
    {
        var rp = realPropertyService.findById(propId);

        return realPropertyDTRepo.findByProperty(rp);
    }

    @PostMapping("/do_save")
    public RealPropertyDealType doSave(@RequestBody RealPropertyDealType value)
    {
        return realPropertyDTRepo.save(value);
    }
}
