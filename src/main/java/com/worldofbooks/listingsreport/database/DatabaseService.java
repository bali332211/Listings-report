package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.database.validation.ListingValidator;
import com.worldofbooks.listingsreport.api.*;
import com.worldofbooks.listingsreport.output.ViolationWriterCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Component
public class DatabaseService {

    private ListingRepository listingRepository;
    private StatusRepository statusRepository;
    private LocationRepository locationRepository;
    private MarketplaceRepository marketplaceRepository;
    private ApiHandler apiHandler;
    @Value(value = "${worldofbooks.api.key}")
    private String apiKey;
    private ListingValidator listingValidator;

    @Autowired
    public DatabaseService(ListingRepository listingRepository,
                           StatusRepository statusRepository,
                           LocationRepository locationRepository,
                           MarketplaceRepository marketplaceRepository,
                           ApiHandler apiHandler, ListingValidator listingValidator) {
        this.listingRepository = listingRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
        this.apiHandler = apiHandler;
        this.listingValidator = listingValidator;
    }

    public void initDatabase() {
        List<Status> statuses = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/listingStatus"), Status.class);
        List<Location> locations = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/location"), Location.class);
        List<Marketplace> marketplaces = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/marketplace"), Marketplace.class);
        List<Listing> listings = apiHandler.getEntitiesFromAPI(constructApiMockarooUri("/listing"), Listing.class);

        saveEntities(statuses, statusRepository);
        saveEntities(locations, locationRepository);
        saveEntities(marketplaces, marketplaceRepository);

        ReferenceDataSet referenceDataSet = new ReferenceDataSet(statuses, locations, marketplaces);
        ListingDataSet listingDataSet = new ListingDataSet(listings, referenceDataSet);

        List<Listing> validatedListings;
        try (ViolationWriterCsv violationWriterCsv = new ViolationWriterCsv()) {
            validatedListings = listingValidator.validateListings(listingDataSet, violationWriterCsv);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

        return uriComponents.toUriString();
    }

}
