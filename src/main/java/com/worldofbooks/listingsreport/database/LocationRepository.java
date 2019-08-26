package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

    @Query("SELECT l.id FROM Location l")
    List<String> findAllIds();

}
