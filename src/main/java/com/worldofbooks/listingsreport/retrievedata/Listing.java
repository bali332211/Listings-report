package com.worldofbooks.listingsreport.retrievedata;

import java.time.Instant;

public class Listing {

    private int id;
    private String title;
    private String description;
    private int inventoryItemLocationId;
    private int listingPrice;
    private String currency;
    private int quantity;
    private int listingStatus;
    private int marketplace;
    private Instant uploadTime;
    private String ownerEmailAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInventoryItemLocationId() {
        return inventoryItemLocationId;
    }

    public void setInventoryItemLocationId(int inventoryItemLocationId) {
        this.inventoryItemLocationId = inventoryItemLocationId;
    }

    public int getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(int listingPrice) {
        this.listingPrice = listingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(int listingStatus) {
        this.listingStatus = listingStatus;
    }

    public int getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(int marketplace) {
        this.marketplace = marketplace;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getOwnerEmailAddress() {
        return ownerEmailAddress;
    }

    public void setOwnerEmailAddress(String ownerEmailAddress) {
        this.ownerEmailAddress = ownerEmailAddress;
    }
}
