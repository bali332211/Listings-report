package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.output.CsvViolationProcessor;

import java.util.List;

public interface ListingValidator {

    List<Listing> validateListings(List<Listing> listings, List<Status> statuses, List<Location> locations, List<Marketplace> marketplaces, CsvViolationProcessor csvViolationProcessor);
}
