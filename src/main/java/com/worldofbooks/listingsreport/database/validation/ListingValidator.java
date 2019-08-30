package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.database.EntityDataSet;
import com.worldofbooks.listingsreport.output.ViolationWriterCsv;

import java.util.List;

public interface ListingValidator {

    List<Listing> validateListings(EntityDataSet entityDataSet, ViolationWriterCsv violationWriterCsv);
}
