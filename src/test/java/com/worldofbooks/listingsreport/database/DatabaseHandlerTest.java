package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.ListingsreportApplication;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ListingsreportApplication.class)
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CommandLineRunner.class))
public class DatabaseHandlerTest {

    @Autowired
    private DatabaseHandler databaseHandler;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MarketplaceRepository marketplaceRepository;

    @Test
    public void saveReferences() {
        Status status = new Status();
        status.setId(4);
        Location location = new Location();
        location.setId("testLocationId");
        Marketplace marketplace = new Marketplace();
        marketplace.setId(2);

        ReferenceDataSet referenceDataSet = new ReferenceDataSet(Arrays.asList(status), Arrays.asList(location), Arrays.asList(marketplace));

        assertFalse(statusRepository.existsById(4));
        assertFalse(locationRepository.existsById("testLocationId"));
        assertFalse(marketplaceRepository.existsById(2));

        databaseHandler.saveReferences(referenceDataSet);

        assertTrue(statusRepository.existsById(4));
        assertTrue(locationRepository.existsById("testLocationId"));
        assertTrue(marketplaceRepository.existsById(2));

    }

}
