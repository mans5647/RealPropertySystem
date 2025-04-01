package com.real_property_system_api.real_property_system.models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "rps_apartments")
@Getter
@Setter
@NoArgsConstructor
public class Apartment {


    @JsonProperty("apartment_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apartmentId;

    @OneToOne
    @JoinColumn(name = "prop_id", referencedColumnName = "propId", unique = true, nullable = false) 
    private RealProperty property;

    @JsonProperty("apartment_floor")
    @Column(name = "apartment_floor")
    private Integer apartmentFloor;


    @JsonProperty("apartment_rooms")
    @Column(name = "apartment_rooms")
    private Integer apartmentRooms;


    @JsonProperty("apartment_build_date")
    @Column(name = "apartment_build_date")
    private LocalDate apartmentBuildDate;


    @JsonProperty("apartment_renovation")
    @Column(name = "apartment_renovation")
    private Boolean apartmentRenovation;


}
