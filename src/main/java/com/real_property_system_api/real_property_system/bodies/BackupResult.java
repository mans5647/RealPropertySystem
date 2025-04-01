package com.real_property_system_api.real_property_system.bodies;

import com.real_property_system_api.real_property_system.responses.GenericMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BackupResult 
{
    private GenericMessage meta;
    private BackupFileInfo file_info;  
}
