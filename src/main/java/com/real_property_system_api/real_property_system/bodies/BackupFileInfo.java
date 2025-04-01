package com.real_property_system_api.real_property_system.bodies;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class BackupFileInfo 
{

    @Column(name = "bf_name")
    private String name;

    @Column(name = "bf_size")
    private long size;

    @Column(name = "bf_server_path")
    private String serverPath;    

    @Column(name = "bf_create_date")
    private LocalDateTime created;
}
