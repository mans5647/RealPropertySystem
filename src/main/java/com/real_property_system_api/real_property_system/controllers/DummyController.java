package com.real_property_system_api.real_property_system.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class DummyController 
{
    @GetMapping("/welcome")
    public ResponseEntity<String> handle_welcome_page()
    {
        return new ResponseEntity<String>("Welcome to OUR API !!!",HttpStatus.OK);
    }

    @GetMapping("/onlyadmin")
    public ResponseEntity<String> indexAdminResource()
    {
        return new ResponseEntity<>("Hello, admin!", HttpStatus.OK);
    }
}
