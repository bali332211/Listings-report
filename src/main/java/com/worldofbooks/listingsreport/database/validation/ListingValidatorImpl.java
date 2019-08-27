package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.output.CsvViolationProcessor;
import com.worldofbooks.listingsreport.output.ViolationProcessor;
import com.worldofbooks.listingsreport.output.ReportProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class ListingValidatorImpl implements ListingValidator {

    private Validator validator;
    private ReportProcessor reportProcessor;

    @Autowired
    public ListingValidatorImpl(Validator validator, ReportProcessor reportProcessor) {
        this.validator = validator;
        this.reportProcessor = reportProcessor;
    }

    @Override
    public List<Listing> validateListings(List<Listing> listings, List<Status> statuses, List<Location> locations, List<Marketplace> marketplaces, CsvViolationProcessor csvViolationProcessor) {
        List<Listing> validatedListings = new ArrayList<>();

        int[] statusIds = getStatusIds(statuses);
        List<String> locationIds = getLocationIds(locations);
        int[] marketplaceIds = getMarketplaceIds(marketplaces);

        listings.forEach(listing -> {
            Set<ConstraintViolation<Listing>> violations = validator.validate(listing);
            List<String> referenceViolations = validateForeignKeyReferences(listing, statusIds, locationIds, marketplaceIds);

            if (violations.isEmpty() && referenceViolations.isEmpty()) {
                validatedListings.add(listing);
            } else {
                csvViolationProcessor.processViolations(violations, referenceViolations, listing);
            }
        });
        reportProcessor.collectReportData(validatedListings);
        return validatedListings;
    }

    private List<String> validateForeignKeyReferences(Listing listing, int[] statusIds, List<String> locationIds, int[] marketplaceIds) {
        List<String> referenceViolations = new ArrayList<>();

        if (!locationIds.contains(listing.getLocationId())) {
            referenceViolations.add("locationId");
        }

        int listingStatus = listing.getListingStatus();
        int listingMarketplace = listing.getMarketplace();
        boolean isStatusIdValid = isIntReferenceValid(listingStatus, statusIds);
        boolean isMarketplaceIdValid = isIntReferenceValid(listingMarketplace, marketplaceIds);

        if (!isStatusIdValid) {
            referenceViolations.add("listingStatus");
        }
        if(!isMarketplaceIdValid) {
            referenceViolations.add("marketplace");
        }
        return referenceViolations;
    }

    private int[] getMarketplaceIds(List<Marketplace> marketplaces) {
        int[] marketplaceIds = new int[marketplaces.size()];

        for (int i = 0; i < marketplaces.size(); i++) {
            Marketplace currentMarketplace = marketplaces.get(i);
            marketplaceIds[i] = currentMarketplace.getId();
        }
        return marketplaceIds;
    }

    private List<String> getLocationIds(List<Location> locations) {
        List<String> locationIds = new ArrayList<>();
        locations.forEach(location -> locationIds.add(location.getId()));
        return locationIds;
    }

    private int[] getStatusIds(List<Status> statuses) {
        int[] statusIds = new int[statuses.size()];

        for (int i = 0; i < statuses.size(); i++) {
            Status currentStatus = statuses.get(i);
            statusIds[i] = currentStatus.getId();
        }
        return statusIds;
    }

    private boolean isIntReferenceValid(int listingReference, int[] references) {
        return Arrays.stream(references).anyMatch(i -> i == listingReference);
    }


}
