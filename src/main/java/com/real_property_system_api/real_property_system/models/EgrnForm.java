package com.real_property_system_api.real_property_system.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
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
@Table(name = "rps_egrn")
@Getter
@Setter
public class EgrnForm 
{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @JsonProperty("address")
    @Column(name = "rp_address")
    private String address;


    @OneToOne
    @JsonProperty("real_property")
    @JoinColumn(name = "real_property")
    private RealProperty realProperty;


    @JsonProperty("reg_date")
    @Column(name = "reg_date")
    private LocalDate registerDate;

    @ManyToOne
    @JsonProperty("deal_prev_type")
    @JoinColumn(name = "deal_type_prev")
    private DealType purchaseType;
}
