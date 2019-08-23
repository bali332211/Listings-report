package com.worldofbooks.listingsreport.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="listings")
public class Listing {

    @Id
    private String id;
    private String title;
    private String description;
    @JsonProperty("inventory_item_location_id")
    private String inventoryItemLocationId;
    @JsonProperty("listing_price")
    private int listingPrice;
    private String currency;
    private int quantity;
    @JsonProperty("listing_status")
    private int listingStatus;
    private int marketplace;
    @JsonProperty("upload_time")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Date uploadTime;
    @JsonProperty("owner_email_address")
    private String ownerEmailAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getInventoryItemLocationId() {
        return inventoryItemLocationId;
    }

    public void setInventoryItemLocationId(String inventoryItemLocationId) {
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

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getOwnerEmailAddress() {
        return ownerEmailAddress;
    }

    public void setOwnerEmailAddress(String ownerEmailAddress) {
        this.ownerEmailAddress = ownerEmailAddress;
    }

    @Override
    public String toString() {
        return getId() + " " +
            getCurrency() + " " +
            getDescription() + " " +
            getUploadTime() + " " +
            getListingPrice() + " " +
            getInventoryItemLocationId() + " " +
            getOwnerEmailAddress() + " " +
            getMarketplace();

    }
}
