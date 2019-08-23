package com.worldofbooks.listingsreport.database;

import java.util.List;

import com.worldofbooks.listingsreport.api.Listing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends CrudRepository<Listing, String> {
    List<Listing> findAll();
}
