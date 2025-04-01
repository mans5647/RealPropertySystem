package com.real_property_system_api.real_property_system.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rps_bs_deal")
@Getter
@Setter
public class BuySellDeal 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1)
    private BigDecimal price;
    private LocalDateTime dateTime;
    
    @ManyToOne
    @JoinColumn(name = "seller_fk")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private User buyer;

    @OneToOne
    @JoinColumn(name = "real_property_fk", nullable = false)
    private RealProperty realProperty;
    private String signPlace;
}
