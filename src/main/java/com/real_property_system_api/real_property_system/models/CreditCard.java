package com.real_property_system_api.real_property_system.models;

import java.math.BigDecimal;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rps_cards")
@Getter
@Setter
public class CreditCard 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonProperty("owner")
    private User owner;
    
    
    @JsonProperty("card_number")
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @JsonProperty("card_cvv")
    @Column(name = "card_cvv", nullable = false)
    private Integer cvv;

    @JsonProperty("card_balance")
    @Column(name = "card_balance", nullable = false)
    private BigDecimal balance;
}
