package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.retrievedata.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
}
