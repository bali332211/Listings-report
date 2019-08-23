package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.retrievedata.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {
}
