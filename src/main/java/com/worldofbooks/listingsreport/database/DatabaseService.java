package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.retrievedata.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseService {

    private ListingRepository listingRepository;

    @Autowired
    public DatabaseService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public void saveListings(List<Listing> listings) {
        for (Listing listing : listings) {
            if (listing.getId() != null) {
                listingRepository.save(listing);
            }
        }
    }

}
