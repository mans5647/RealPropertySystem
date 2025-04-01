package com.real_property_system_api.real_property_system.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.real_property_system_api.real_property_system.models.ServerLog;
import com.real_property_system_api.real_property_system.repos.ServerLogRepository;


@Service
public class ServerLogService 
{
    @Autowired
    private ServerLogRepository repository;


    
    public Page<ServerLog> getChunk(Pageable pageable)
    {
        return repository.findAll(pageable);
    }

    private ServerLog Add(ServerLog log)
    {
        return repository.save(log);
    }

    public void Info(String c)
    {
        var log = ServerLog.createInfo(c);
        Add(log);
    }

    public void Warn(String c)
    {
        var log = ServerLog.createWarn(c);
        Add(log);
    }

    public void Error(String c)
    {
        var log = ServerLog.createErr(c);
        Add(log);
    }

    public void Fatal(String c)
    {
        var log = ServerLog.createFatal(c);
        Add(log);
    }

    public Long GetTotalCount()
    {
        return repository.GetTotalCount();
    }
}
