package com.real_property_system_api.real_property_system.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.bodies.SingleValue;
import com.real_property_system_api.real_property_system.models.RealProperty;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.services.RealPropertyService;
import com.real_property_system_api.real_property_system.services.UserService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/resources/props")
public class RealPropertyController 
{
    @Autowired
    private RealPropertyService realPropertyService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<RealProperty> fetchAll(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        var data = realPropertyService.findAllPaged(pageable);

        return (data.hasContent()) ? data.getContent() : Collections.emptyList();

    }

    @PostMapping("/add")
    public ResponseEntity<SingleValue<RealProperty>> addNewProperty(@RequestBody RealProperty value)
    {
        if (realPropertyService.existsByTitle(value.getPropTitle()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(SingleValue.<RealProperty>of(null));
        }

        var savedInstance = realPropertyService.save(value);
        
        return ResponseEntity.ok()
            .body(SingleValue.<RealProperty>of(savedInstance));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<SingleValue<RealProperty>> updatePropertyById(@PathVariable("id") Long id, @RequestBody RealProperty updValue)
    {

        var value = realPropertyService.findById(id);

        if (value == null)
        {
            return ResponseEntity.notFound()    
            .build();
        }

        var updatedInstance = realPropertyService.save(updValue);

        return ResponseEntity.ok()
            .body(SingleValue.<RealProperty>of(updatedInstance));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id)
    {

        if (!realPropertyService.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        realPropertyService.deleteById(id);

        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/check_user_request_policy")
    public String doesUserCanRequestProperty(@RequestParam("uid") Long userId, 
        @RequestParam("prop_id") Long propId,
        HttpServletResponse resp)
    {
        final User user = userService.findById(userId);
        final RealProperty realProperty = realPropertyService.findById(propId);

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        if (user.getLogin().equals(realProperty.getSeller().getLogin()))
        {
            return "Вы не можете подать заявку на свое обьявление";
        }

        else if (user.getPassport() == null)
        {
            return "Паспортные данные не заполнены";
        }

        final String firstname = user.getFirstName();
        final String lastname = user.getLastName();

        if (firstname != null)
        {
            if (firstname.isEmpty() || firstname.isBlank())
            {
                return "Имя пустое";
            }
        }
        else 
        {
            return "Имя не заполнено";
        }

        if (lastname != null)
        {
            if (lastname.isEmpty() || lastname.isBlank())
            {
                return "Фамилия пустая";
            }
        }
        else 
        {
            return "Фамилия не заполнена";
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        return null;
    }

}
