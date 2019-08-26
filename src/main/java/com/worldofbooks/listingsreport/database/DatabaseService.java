package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.ReportProcessor;
import com.worldofbooks.listingsreport.ReportUtil;
import com.worldofbooks.listingsreport.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
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

    @Autowired
    public DatabaseService(ListingRepository listingRepository,
                           StatusRepository statusRepository,
                           LocationRepository locationRepository,
                           MarketplaceRepository marketplaceRepository,
                           ApiHandler apiHandler,
                           Validator validator, ReportUtil reportUtil, ReportProcessor reportProcessor) {
        this.listingRepository = listingRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
        this.apiHandler = apiHandler;
        this.validator = validator;
        this.reportProcessor = reportProcessor;
    }

    public void initDatabase() {
        List<Status> statuses = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/listingStatus"), Status.class);
        List<Location> locations = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/location"), Location.class);
        List<Marketplace> marketplaces = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/marketplace"), Marketplace.class);
        List<Listing> listings = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/listing"), Listing.class);

        List<Listing> validatedListings = validateListings(listings, statuses, locations, marketplaces);
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


        listings.forEach(listing -> {
            Set<ConstraintViolation<Listing>> violations = validator.validate(listing);
            List<String> referenceViolations = validateForeignKeyReferences();

            if (locationIds.contains(listing.getLocationId())) {
                referenceViolations.add("locationId");
            }

            if (violations.isEmpty()) {
                validatedListings.add(listing);
            } else {
                writeViolationsToCSV(violations, referenceViolations, listing);
            }


        });
        reportProcessor.collectReportData(validatedListings);
        return validatedListings;
    }


    private int[] getStatusIds(List<Status> statuses) {
        int[] statusIds = new int[statuses.size()];

        for (int i = 0; i < statuses.size(); i++) {
            Status currentStatus = statuses.get(i);
            statusIds[i] = currentStatus.getId();
        }
        return statusIds;
    }

    private void writeViolationsToCSV(Set<ConstraintViolation<Listing>> violations, List<String> referenceViolations, Listing listing) {
        List<ViolationDto> violationDtos = getViolationDtosForCSV(violations, referenceViolations, listing);

    }

    private List<ViolationDto> getViolationDtosForCSV(Set<ConstraintViolation<Listing>> violations, List<String> referenceViolations, Listing listing) {
        List<ViolationDto> violationDtos = new ArrayList<>();
        String id = listing.getId();
        int marketplace = listing.getMarketplace();

        violations.forEach(violation -> {
            ViolationDto violationDto = new ViolationDto(
                id,
                marketplace,
                violation.getPropertyPath().toString());

            violationDtos.add(violationDto);
        });

        referenceViolations.forEach(violation -> {
            ViolationDto violationDto = new ViolationDto(
                id,
                marketplace,
                violation);

            violationDtos.add(violationDto);
        });
        return violationDtos;
    }

    private static final class ViolationDto {
        private final String listingId;
        private final int marketplaceName;
        private final String fieldName;

        public ViolationDto(String listingId, int marketplaceName, String fieldName) {
            this.listingId = listingId;
            this.marketplaceName = marketplaceName;
            this.fieldName = fieldName;
        }

        public String getListingId() {
            return listingId;
        }

        public int getMarketplaceName() {
            return marketplaceName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }



}
