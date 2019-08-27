package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

}
