package com.real_property_system_api.real_property_system.models;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rps_real_properties")
@Getter
@Setter
public class RealProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propId;

    @Column(name = "prop_type", nullable = false)
    private String propType;

    @Column(name = "prop_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal propPrice;

    @Column(name = "prop_title", length = Integer.MAX_VALUE)
    
    private String propTitle;

    @Column(name = "prop_description", length = Integer.MAX_VALUE)
    private String propDescription;


    @Column(name = "prop_image_url", length = Integer.MAX_VALUE)
    private String propImageUrl;


    @ManyToOne
    @JoinColumn(name = "prop_seller", referencedColumnName = "uid")
    private User seller;

    @Column(name = "prop_area", precision = 10, scale = 2)
    private BigDecimal propArea;

    @JsonIgnore
    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Apartment apartment;

    @JsonIgnore
    @OneToOne(mappedBy = "property", orphanRemoval = true, cascade = CascadeType.ALL)
    private RealPropertyDealType realPropertyDealType;

    @JsonIgnore
    @OneToOne(mappedBy = "property", orphanRemoval = true, cascade = CascadeType.ALL)
    private Deal deal;

    @JsonIgnore
    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private LandPlot landPlot;

    public static final String PROP_TYPE_APARTMENT = "apt", PROP_TYPE_LAND = "lnd", PROP_TYPE_UNDEF = "none";
}
