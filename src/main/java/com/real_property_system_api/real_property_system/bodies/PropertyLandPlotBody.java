package com.real_property_system_api.real_property_system.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.real_property_system_api.real_property_system.models.LandPlot;
import com.real_property_system_api.real_property_system.models.RealProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PropertyLandPlotBody 
{
    private RealProperty parent;
    private LandPlot child;

    @JsonProperty("land_category_id")
    private Long landCategoryId;

    @JsonProperty("land_soil_id")
    private Long landSoilId;
}
