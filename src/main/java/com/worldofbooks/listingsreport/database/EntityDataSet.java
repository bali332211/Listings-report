package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public class EntityDataSet {
    private List<Listing> listings;
    private ReferenceDataSet referenceDataSet;

    public EntityDataSet(List<Listing> listings, ReferenceDataSet referenceDataSet) {
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
