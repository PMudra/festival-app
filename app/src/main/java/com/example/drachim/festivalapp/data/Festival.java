package com.example.drachim.festivalapp.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Festival implements Serializable {

    private int id;
    private String name;
    private String description;
    private String country;
    private String place;
    private String postalCode;
    private String street;
    private Double latitude;
    private Double longitude;
    private Date startDate;
    private Date endDate;
    private List<String> lineup;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<String> getLineup() {
        return lineup;
    }
}
