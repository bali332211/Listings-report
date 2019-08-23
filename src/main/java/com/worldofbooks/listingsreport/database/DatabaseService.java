package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.retrievedata.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DatabaseService {

    private ListingRepository listingRepository;
    private StatusRepository statusRepository;
    private LocationRepository locationRepository;
    private MarketplaceRepository marketplaceRepository;
    private ApiHandler apiHandler;

    private final RestTemplate restTemplate;

    @Autowired
    public DatabaseService(ListingRepository listingRepository, StatusRepository statusRepository, LocationRepository locationRepository, MarketplaceRepository marketplaceRepository, ApiHandler apiHandler) {
        this.listingRepository = listingRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
        this.apiHandler = apiHandler;
        this.restTemplate = apiHandler.restTemplate();
    }

    public void initListings() {
        List<Listing> listings = apiHandler.getEntitiesFromAPI(restTemplate, "https://my.api.mockaroo.com/listing?key=63304c70");

        for (Listing listing : listings) {
            if (listing.getId() != null) {
                listingRepository.save(listing);
            }
        }
    }

    public void initStatuses() {
//        List<Status> statuses = apiHandler.getEntitiesFromAPI(restTemplate, "https://my.api.mockaroo.com/listingStatus?key=63304c70");
        List<Status> statuses = apiHandler.getStatuses(restTemplate);

        for (Status status : statuses) {
            statusRepository.save(status);
        }
    }

    public void initLocations() {
        List<Location> locations = apiHandler.getEntitiesFromAPI(restTemplate, "https://my.api.mockaroo.com/location?key=63304c70");

        for (Location location : locations) {
            locationRepository.save(location);
        }
    }

    public void initMarketplaces() {
        List<Marketplace> marketplaces = apiHandler.getEntitiesFromAPI(restTemplate, "https://my.api.mockaroo.com/marketplace?key=63304c70");

        for (Marketplace marketplace : marketplaces) {
            marketplaceRepository.save(marketplace);
        }
    }

//    public <T, IDTYPE> void initEntities(String url, String idType) {
//        List<T> entities = apiHandler.getEntitiesFromAPI(restTemplate, url);
//
//        CrudRepository<T, IDTYPE> repository;
//        for (T entity: entities) {
//            repository.save(entity);
//        }
//    }

}
