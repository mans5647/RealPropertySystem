package com.real_property_system_api.real_property_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "rps_soiltypes")
public class SoilType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("soil_id")
    private Long soilId;

    @Column(name = "soil_name", nullable = false)
    @JsonProperty("name")
    private String soilName;

    // Getters and setters
}
