package com.real_property_system_api.real_property_system.configs;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String resourceNotFoundException(NoResourceFoundException ex) {
        return "No such route!";   
    }


}