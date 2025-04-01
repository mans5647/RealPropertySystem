package com.real_property_system_api.real_property_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rps_landplots")
@Getter
@Setter
public class LandPlot {

    @JsonProperty("plot_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plotId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prop_id", nullable = false)
    private RealProperty property;

    @JsonProperty("land_soil_type")
    @ManyToOne
    @JoinColumn(name = "plot_soil_type", referencedColumnName = "soilId")
    private SoilType soilType;

    @JsonProperty("land_category")
    @ManyToOne
    @JoinColumn(name = "plot_land_category", referencedColumnName = "categoryId")
    private LandCategory landCategory;

    @JsonProperty("plot_location")
    @Column(name = "plot_location")
    private String plotLocation; // Primitive String for the location description of the plot


    @JsonProperty("plot_latitude")
    @Column(name = "plot_latitude")
    private Double plotLatitude; // Primitive Double for the latitude of the land plot (GPS coordinate)

    @JsonProperty("plot_longitude")
    @Column(name = "plot_longitude")
    private Double plotLongitude; // Primitive Double for the longitude of the land plot (GPS coordinate)


    @JsonProperty("plot_road_access")
    @Column(name = "plot_road_access")
    private Boolean roadAccess;  // Whether the plot has road access (true/false)

    @JsonProperty("plot_water_access")
    @Column(name = "plot_water_access")
    private Boolean waterAccess;  // Whether the plot has access to water resources (true/false)


    @JsonProperty("plot_environmental_status")
    @Column(name = "plot_environmental_status")
    private String environmentalStatus;  // Environmental status (e.g., clean, contaminated)


    // Getters and setters
}