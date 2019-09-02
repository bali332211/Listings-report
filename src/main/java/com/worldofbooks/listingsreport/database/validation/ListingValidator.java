package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.database.ListingDataSet;
import com.worldofbooks.listingsreport.output.ViolationWriterCsv;

import java.util.List;

public interface ListingValidator {

    ListingValidationResult validateListings(ListingDataSet listingDataSet);
}
