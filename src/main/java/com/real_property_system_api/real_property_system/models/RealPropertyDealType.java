package com.real_property_system_api.real_property_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "rps_property_deal_type")
@Getter
@Setter
public class RealPropertyDealType 
{

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @JsonProperty("prop")

    @OneToOne
    @JoinColumn(name = "fk_property", nullable = false)
    private RealProperty property;

    @JsonProperty("desired_type")
    @ManyToOne
    @JoinColumn(name = "fk_desired_type", nullable = false)
    private DealType desiredType;
}
