package com.real_property_system_api.real_property_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "rps_landcategories")
public class LandCategory {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @JsonProperty("name")
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    // Getters and setters
}
