package com.real_property_system_api.real_property_system.bodies;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageContent<T> 
{
    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("data")
    private List<T> data;

}
