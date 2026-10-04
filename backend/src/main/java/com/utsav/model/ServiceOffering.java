package com.utsav.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * One priced service a vendor offers, e.g. ("Full mandap decor", 1200, "event").
 * Stored as an element collection on Vendor — services never exist without
 * their vendor, so a separate table with its own lifecycle would be overkill.
 */
@Embeddable
public class ServiceOffering {

    private String name;

    @Column(name = "service_price")
    private int price;

    /** Pricing unit as shown in the frontend: event | person | session | package | hour | setup | add-on */
    private String unit;

    public ServiceOffering() {
    }

    public ServiceOffering(String name, int price, String unit) {
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
