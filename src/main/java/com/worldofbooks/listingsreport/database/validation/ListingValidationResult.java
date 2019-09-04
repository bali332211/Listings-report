package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public class ListingValidationResult {
    private List<Listing> validatedListings;
    private List<ViolationDataSet> violationDataSets;

    public ListingValidationResult() {
    }

    public ListingValidationResult(List<Listing> validatedListings, List<ViolationDataSet> violationDataSets) {
        this.validatedListings = validatedListings;
        this.violationDataSets = violationDataSets;
    }

    public List<Listing> getValidatedListings() {
        return validatedListings;
    }

    public void setValidatedListings(List<Listing> validatedListings) {
        this.validatedListings = validatedListings;
    }

    public List<ViolationDataSet> getViolationDataSets() {
        return violationDataSets;
    }

    public void setViolationDataSets(List<ViolationDataSet> violationDataSets) {
        this.violationDataSets = violationDataSets;
    }
}
