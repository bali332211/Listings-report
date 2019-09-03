package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.ListingDataSet;

public interface ListingValidator {

    ListingValidationResult validateListings(ListingDataSet listingDataSet);
}
