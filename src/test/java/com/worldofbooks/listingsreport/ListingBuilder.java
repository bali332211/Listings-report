package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.api.Listing;
import java.time.LocalDate;

public class ListingBuilder {

    private String id;
    private String title;
    private String description;
    private String locationId;
    private double listingPrice;
    private String currency;
    private int quantity;
    private int listingStatus;
    private int marketplace;
    private LocalDate uploadTime;
    private String ownerEmailAddress;

    public ListingBuilder(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public ListingBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ListingBuilder locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public ListingBuilder listingPrice(double listingPrice) {
        this.listingPrice = listingPrice;
        return this;
    }

    public ListingBuilder currency(String currency) {
        this.currency = currency;
        return this;
    }

    public ListingBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ListingBuilder listingStatus(int listingStatus) {
        this.listingStatus = listingStatus;
        return this;
    }

    public ListingBuilder marketplace(int marketplace) {
        this.marketplace = marketplace;
        return this;
    }

    public ListingBuilder uploadTime(LocalDate uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public ListingBuilder ownerEmailAddress(String ownerEmailAddress) {
        this.ownerEmailAddress = ownerEmailAddress;
        return this;
    }

    public Listing createListing() {
        return new Listing(
                id, title, description,
                locationId, listingPrice,
                currency, quantity, listingStatus,
                marketplace, uploadTime, ownerEmailAddress);
    }



}
