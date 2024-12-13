package com.real_property_system_api.real_property_system.models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "rps_passports")
public class Passport 
{
    @Id
    @GeneratedValue
    @Column(name = "p_id")
    private Long id;

    @Column(name = "p_division_code")
    private String divisionCode;

    @Column(name = "p_series")
    private int pSeries;
    
    @Column(name = "p_number")
    private int pNumber;
    
    @Column(name = "p_sex")
    private boolean pSex;
    
    @Column(name = "p_given_by")
    private String pGivenBy;
    
    @Column(name = "p_given_date")
    private Date pGivenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public int getpSeries() {
        return pSeries;
    }

    public void setpSeries(int pSeries) {
        this.pSeries = pSeries;
    }

    public int getpNumber() {
        return pNumber;
    }

    public void setpNumber(int pNumber) {
        this.pNumber = pNumber;
    }

    public boolean ispSex() {
        return pSex;
    }

    public void setpSex(boolean pSex) {
        this.pSex = pSex;
    }

    public String getpGivenBy() {
        return pGivenBy;
    }

    public void setpGivenBy(String pGivenBy) {
        this.pGivenBy = pGivenBy;
    }

    public Date getpGivenDate() {
        return pGivenDate;
    }

    public void setpGivenDate(Date pGivenDate) {
        this.pGivenDate = pGivenDate;
    }


    

}
