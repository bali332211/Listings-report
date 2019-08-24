package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.*;
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
    private ValidationService validationService;

    @Autowired
    public DatabaseService(ListingRepository listingRepository,
                           StatusRepository statusRepository,
                           LocationRepository locationRepository,
                           MarketplaceRepository marketplaceRepository,
                           ApiHandler apiHandler,
                           ValidationService validationService) {
        this.listingRepository = listingRepository;
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
        this.apiHandler = apiHandler;
        this.validationService = validationService;
    }

    public void initDatabase() {
        initEntities("https://my.api.mockaroo.com/listingStatus?key=63304c70", Status.class, statusRepository);
        initEntities("https://my.api.mockaroo.com/location?key=63304c70", Location.class, locationRepository);
        initEntities("https://my.api.mockaroo.com/marketplace?key=63304c70", Marketplace.class, marketplaceRepository);
        initListings();
    }

    private <T> void initEntities(String url, Class<T> tClass, CrudRepository<T, ?> repository) {
        List<T> entities = apiHandler.getEntitiesFromAPI(url, tClass);

        for (T entity : entities) {
            repository.save(entity);
        }
    }

    private void initListings() {
        List<Listing> listings = apiHandler.getEntitiesFromAPI("https://my.api.mockaroo.com/listing?key=63304c70", Listing.class);

        List<Listing> validatedListings = validationService.validateListings(listings);

        for (Listing listing : validatedListings) {
            if (listing.getId() != null) {
                listingRepository.save(listing);
            }
        }
    }

}
