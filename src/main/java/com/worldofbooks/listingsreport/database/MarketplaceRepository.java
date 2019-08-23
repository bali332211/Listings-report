package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.retrievedata.Marketplace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketplaceRepository extends CrudRepository<Marketplace, Integer> {
}
