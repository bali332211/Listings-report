package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public class ListingValidationResult {
    private List<Listing> validatedListings;
    private List<ViolationDataSet> violationDataSets;

    public ListingValidationResult(List<Listing> validatedListings, List<ViolationDataSet> violationDataSets) {
        this.validatedListings = validatedListings;
        this.violationDataSets = violationDataSets;
    }

    public List<Listing> getValidatedListings() {
        return validatedListings;
    }

    public List<ViolationDataSet> getViolationDataSets() {
        return violationDataSets;
    }
}
