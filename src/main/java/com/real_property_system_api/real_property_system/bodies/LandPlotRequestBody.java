package com.real_property_system_api.real_property_system.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.real_property_system_api.real_property_system.models.LandPlot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LandPlotRequestBody 
{
    private LandPlot value;
    
    @JsonProperty("soil_id")
    private Long soilId;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("prop_id")
    private Long propertyId;

}
