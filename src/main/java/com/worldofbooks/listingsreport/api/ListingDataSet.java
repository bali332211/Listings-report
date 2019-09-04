package com.worldofbooks.listingsreport.api;

import com.worldofbooks.listingsreport.database.ReferenceDataSet;

import java.util.List;

public class ListingDataSet {
    private List<Listing> listings;
    private ReferenceDataSet referenceDataSet;

    public ListingDataSet() {
    }

    public ListingDataSet(List<Listing> listings, ReferenceDataSet referenceDataSet) {
        this.listings = listings;
        this.referenceDataSet = referenceDataSet;
    }

    public List<Listing> getListings() {
        return listings;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }

    public ReferenceDataSet getReferenceDataSet() {
        return referenceDataSet;
    }

    public void setReferenceDataSet(ReferenceDataSet referenceDataSet) {
        this.referenceDataSet = referenceDataSet;
    }
}
