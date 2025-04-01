package com.real_property_system_api.real_property_system.models;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;


@Entity
@Table(name = "server_logs")
@Getter
public class ServerLog 
{
    private ServerLog(Instant logTime, Integer logLevel, String logContent)
    {
        this.logTime = logTime;
        this.logLevel = logLevel;
        this.logContent = logContent;
    }

    public ServerLog()
    {

    }

    public static ServerLog invalid()
    {
        var slog = new ServerLog();
        slog.logId = -1L;
        return slog;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long logId;

    @JsonProperty("time")
    @Column(name = "log_time")
    private Instant logTime;


    @JsonProperty("level")
    @Min(value = 0)
    @Max(value = 5)
    private Integer logLevel;
    
    @JsonProperty("content")
    @Column(name = "log_content")
    @Max(value = 500)
    private String logContent;

    public static final int LOG_TRACE = 0;
    public static final int LOG_DEBUG = 1;
    public static final int LOG_INFO = 2;
    public static final int LOG_WARN = 3;
    public static final int LOG_ERROR = 4;
    public static final int LOG_FATAL = 5;


    public static ServerLog create(String content, int level)
    {
        return new ServerLog(Instant.now(), level, content);
    }

    public static ServerLog createDebug(String c)
    {
        return new ServerLog(Instant.now(), LOG_DEBUG, c);
    }

    public static ServerLog createInfo(String c)
    {
        return new ServerLog(Instant.now(), LOG_INFO, c);
    }
    
    public static ServerLog createWarn(String c)
    {
        return new ServerLog(Instant.now(), LOG_WARN, c);
    }
    
    public static ServerLog createErr(String c)
    {
        return new ServerLog(Instant.now(), LOG_ERROR, c);
    }
    
    public static ServerLog createFatal(String c)
    {
        return new ServerLog(Instant.now(), LOG_FATAL, c);
    }

    public static String fmt(String fmt,Object ... args)
    {
        return String.format(fmt, args);
    }
}
