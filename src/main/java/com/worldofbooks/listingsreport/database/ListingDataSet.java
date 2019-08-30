package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public class ListingDataSet {
    private List<Listing> listings;
    private ReferenceDataSet referenceDataSet;

    public ListingDataSet(List<Listing> listings, ReferenceDataSet referenceDataSet) {
        this.listings = listings;
        this.referenceDataSet = referenceDataSet;
    }

    public List<Listing> getListings() {
        return listings;
    }

    public ReferenceDataSet getReferenceDataSet() {
        return referenceDataSet;
    }
}
