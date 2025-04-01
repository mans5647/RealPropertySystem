package com.real_property_system_api.real_property_system.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real_property_system_api.real_property_system.models.ServerLog;


public interface ServerLogRepository extends PagingAndSortingRepository<ServerLog, Long>
{
    public ServerLog save(ServerLog serverLog);

    @Query(value = "SELECT COUNT(*) FROM server_logs", nativeQuery = true)
    public Long GetTotalCount();
}
