package com.worldofbooks.listingsreport.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.worldofbooks.listingsreport.database.validation.DecimalConstraint;
import com.worldofbooks.listingsreport.database.validation.UUIDConstraint;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @NotNull
    @UUIDConstraint
    private String id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    @JsonProperty("location_id")
    private String locationId;
    @NotNull
    @DecimalConstraint
    @Min(value = 1, message = "only above 0 allowed")
    @JsonProperty("listing_price")
    private double listingPrice;
    @NotNull
    @Size(min = 3, max = 3, message = "currency length needs to be 3")
    private String currency;
    @NotNull
    @Min(value = 1, message = "only above 0 allowed")
    private int quantity;
    @NotNull
    @JsonProperty("listing_status")
    private int listingStatus;
    @NotNull
    private int marketplace;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("upload_time")
    private LocalDate uploadTime;
    @NotNull
    @Email
    @JsonProperty("owner_email_address")
    private String ownerEmailAddress;

    public Listing() {
    }

    public Listing(String id, String title, String description, String locationId, double listingPrice, String currency, int quantity, int listingStatus, int marketplace, LocalDate uploadTime, String ownerEmailAddress) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.locationId = locationId;
        this.listingPrice = listingPrice;
        this.currency = currency;
        this.quantity = quantity;
        this.listingStatus = listingStatus;
        this.marketplace = marketplace;
        this.uploadTime = uploadTime;
        this.ownerEmailAddress = ownerEmailAddress;
    }

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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public double getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(double listingPrice) {
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

    public LocalDate getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDate uploadTime) {
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
            getLocationId() + " " +
            getOwnerEmailAddress() + " " +
            getMarketplace();
    }
}
