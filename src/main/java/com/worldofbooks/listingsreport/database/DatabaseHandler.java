package com.worldofbooks.listingsreport.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseHandler {

    private StatusRepository statusRepository;
    private LocationRepository locationRepository;
    private MarketplaceRepository marketplaceRepository;

    @Autowired
    public DatabaseHandler(StatusRepository statusRepository, LocationRepository locationRepository,
                           MarketplaceRepository marketplaceRepository) {
        this.statusRepository = statusRepository;
        this.locationRepository = locationRepository;
        this.marketplaceRepository = marketplaceRepository;
    }

    public void saveReferences(ReferenceDataSet referenceDataSet) {
        saveEntities(referenceDataSet.getStatuses(), statusRepository);
        saveEntities(referenceDataSet.getLocations(), locationRepository);
        saveEntities(referenceDataSet.getMarketplaces(), marketplaceRepository);
    }

    public <T> void saveEntities(List<T> entities, CrudRepository<T, ?> repository) {
        for (T entity : entities) {
            repository.save(entity);
        }
    }

}
