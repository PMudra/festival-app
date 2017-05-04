package com.example.drachim.festivalapp.data;

import java.util.Date;
import java.util.List;

/**
 * Created by Dr. Achim on 04.05.2017.
 */

public class Festival {
    private final String name;

    /**
     *  adress fields
     */
    private final String country;
    private final String place;
    private final String postalCode;
    private String street;
    private Integer houseNumber;

    private Date startDate;
    private Date endDate;

    private List<String> lineup;

    public Festival(String name, String country, String place, String postalCode) {
        this.name = name;
        this.country = country;
        this.place = place;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getPlace() {
        return place;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getLineup() {
        return lineup;
    }

    public void setLineup(List<String> lineup) {
        this.lineup = lineup;
    }
}
