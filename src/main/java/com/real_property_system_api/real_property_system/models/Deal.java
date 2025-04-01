package com.real_property_system_api.real_property_system.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "rps_deals")
@Getter
@Setter
public class Deal 
{

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dealId;

    @JsonProperty("property")
    @OneToOne
    @JoinColumn(name = "deal_property", referencedColumnName = "propId", nullable = false)
    private RealProperty property;

    @JsonProperty("buyer")
    @ManyToOne
    @JoinColumn(name = "deal_buyer", referencedColumnName = "uid", nullable = false)
    private User buyer;


    @JsonProperty("realtor")
    @ManyToOne
    @JoinColumn(name = "deal_realtor")
    private User realtor;


    @JsonProperty("deal_date")
    @Column(name = "deal_date", nullable = true)
    private LocalDateTime dealDate;

    @JsonProperty("deal_sign_time")
    @Column(name = "deal_sign_time", nullable = true)
    private LocalDateTime dealSignTime;

    @JsonProperty("deal_price")
    @Column(name = "deal_price", nullable = true)
    private BigDecimal dealPrice;


    @JsonProperty("deal_status")
    @ManyToOne
    @JoinColumn(name = "deal_status", nullable = false)
    private DealStatus dealStatus;

    @JsonProperty("deal_type")
    @ManyToOne
    @JoinColumn(name = "deal_type", nullable = false)
    private DealType dealType;

    
    @JsonProperty("docs_checked")
    @Column(name = "is_docs_checked", nullable = true)
    private Boolean docsChecked;
}
