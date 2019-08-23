package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.retrievedata.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseService {

    private ListingRepository listingRepository;
    private StatusRepository statusRepository;
    private LocationRepository locationRepository;
    private MarketplaceRepository marketplaceRepository;
    private ApiHandler apiHandler;

    @Autowired
    public DatabaseService(ListingRepository listingRepository, StatusRepository statusRepository, LocationRepository locationRepository, MarketplaceRepository marketplaceRepository, ApiHandler apiHandler) {
        this.listingRepository = listingRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
        this.apiHandler = apiHandler;
    }

    public void initListings() {
        List<Listing> listings = apiHandler.getEntitiesFromAPI("https://my.api.mockaroo.com/listing?key=63304c70", Listing.class);

        for (Listing listing : listings) {
            if (listing.getId() != null) {
                listingRepository.save(listing);
            }
        }
    }

    public void initStatuses() {
        List<Status> statuses = apiHandler.getEntitiesFromAPI("https://my.api.mockaroo.com/listingStatus?key=63304c70", Status.class);

        for (Status status : statuses) {
            statusRepository.save(status);
        }
    }

    public void initLocations() {
        List<Location> locations = apiHandler.getEntitiesFromAPI("https://my.api.mockaroo.com/location?key=63304c70", Location.class);

        for (Location location : locations) {
            locationRepository.save(location);
        }
    }

    public void initMarketplaces() {
        List<Marketplace> marketplaces = apiHandler.getEntitiesFromAPI("https://my.api.mockaroo.com/marketplace?key=63304c70", Marketplace.class);

        for (Marketplace marketplace : marketplaces) {
            marketplaceRepository.save(marketplace);
        }
    }

    public <T> void initEntities(String url, Class<T> tClass, CrudRepository<T, ?> repository) {
        List<T> entities = apiHandler.getEntitiesFromAPI(url, tClass);

        for (T entity: entities) {
            repository.save(entity);
        }
    }

}
