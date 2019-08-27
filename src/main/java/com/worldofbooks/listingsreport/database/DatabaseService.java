package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.output.CsvProcessor;
import com.worldofbooks.listingsreport.output.ReportProcessor;
import com.worldofbooks.listingsreport.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseService {

    private ListingRepository listingRepository;
    private StatusRepository statusRepository;
    private LocationRepository locationRepository;
    private MarketplaceRepository marketplaceRepository;
    private ApiHandler apiHandler;
    @Value(value = "${worldofbooks.api.key}")
    private String apiKey;
    private Validator validator;
    private ReportProcessor reportProcessor;
    private CsvProcessor csvProcessor;

    @Autowired
    public DatabaseService(ListingRepository listingRepository,
                           StatusRepository statusRepository,
                           LocationRepository locationRepository,
                           MarketplaceRepository marketplaceRepository,
                           ApiHandler apiHandler,
                           Validator validator,
                           ReportProcessor reportProcessor,
                           CsvProcessor csvProcessor) {
        this.listingRepository = listingRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
        this.apiHandler = apiHandler;
        this.validator = validator;
        this.reportProcessor = reportProcessor;
        this.csvProcessor = csvProcessor;
    }

    public void initDatabase() {
        List<Status> statuses = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/listingStatus"), Status.class);
        List<Location> locations = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/location"), Location.class);
        List<Marketplace> marketplaces = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/marketplace"), Marketplace.class);
        List<Listing> listings = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/listing"), Listing.class);

        saveEntities(statuses, statusRepository);
        saveEntities(locations, locationRepository);
        saveEntities(marketplaces, marketplaceRepository);

        List<Listing> validatedListings = validateListings(listings, statuses, locations, marketplaces);
        saveEntities(validatedListings, listingRepository);
    }

    private <T> void saveEntities(List<T> entities, CrudRepository<T, ?> repository) {
        for (T entity : entities) {
            repository.save(entity);
        }
    }

    private String constructApiMockarooUri(String path) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host("my.api.mockaroo.com").path(path).query("key={keyword}").buildAndExpand(apiKey);

        String uriString = uriComponents.toUriString();
        return uriString;
    }

    public List<Listing> validateListings(List<Listing> listings, List<Status> statuses, List<Location> locations, List<Marketplace> marketplaces) {
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
                csvProcessor.processViolations(violations, referenceViolations, listing);
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
