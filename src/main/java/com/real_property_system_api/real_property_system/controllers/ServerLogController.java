package com.real_property_system_api.real_property_system.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.bodies.SingleValue;
import com.real_property_system_api.real_property_system.models.ServerLog;
import com.real_property_system_api.real_property_system.services.ServerLogService;

@RestController
@RequestMapping("/api/resources/logs")
public class ServerLogController 
{

    @Autowired
    private ServerLogService logService;

    @GetMapping("/ch")
    public List<ServerLog> pageableRequest(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "5", name = "size") int size)
    {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<ServerLog> chunk = logService.getChunk(pageRequest);

        return chunk.hasContent() ? chunk.getContent() : Collections.emptyList();

    }    

    @GetMapping("total")
    public ResponseEntity<SingleValue<Long>> GetTotalCount()
    {
        SingleValue<Long> value = SingleValue.<Long>of(logService.GetTotalCount());

        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
