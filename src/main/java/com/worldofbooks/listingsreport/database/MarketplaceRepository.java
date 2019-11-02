package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Marketplace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketplaceRepository extends CrudRepository<Marketplace, Integer> {
    List<Marketplace> findAll();

    Marketplace findByMarketplaceName(String marketplaceName);
}
