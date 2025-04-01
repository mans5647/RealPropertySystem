package com.real_property_system_api.real_property_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rps_deal_types")
@Getter
@Setter
public class DealType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("type_id")
    private Long typeId;

    @Column(name = "type_name", nullable = false)
    @JsonProperty("type_name")
    private String typeName;

    @JsonProperty("type_tech")
    @Column(name = "type_tech")
    private Integer typeTech;

    public static final String 
        TYPE_BUY = "Купля-продажа",
        TYPE_RENT = "Аренда";

    public static final int
        TYPE_TECH_BUY = 0,
        TYPE_TECH_RENT = 1;
}
