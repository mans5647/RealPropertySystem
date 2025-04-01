package com.real_property_system_api.real_property_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.models.Passport;
import com.real_property_system_api.real_property_system.services.PassportService;



@RestController
@RequestMapping("/api/resources/passports")
public class PassportController
{

    @Autowired
    private PassportService passportService;

    @PostMapping("/add")
    public Passport SavePassport(@RequestBody Passport p)
    {
        return passportService.Save(p);
    }

    @GetMapping("/get/{id}")
    public Passport GetPassportById(@PathVariable("id") Long id)
    {
        if (passportService.GetById(id).isPresent())
        {
            return passportService.GetById(id).get();
        }
        
        return null;
    }


    @PutMapping("/upd/{id}")
    public Passport UpdatePassportById(@PathVariable("id") Long id, @RequestBody Passport p)
    {
        if (passportService.GetById(id).isPresent())
        {
            return passportService.Update(p).get();
        }

        return null;
    }

}
