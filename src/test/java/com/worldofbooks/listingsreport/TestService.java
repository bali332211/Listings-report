package com.worldofbooks.listingsreport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.database.ListingDataSet;
import com.worldofbooks.listingsreport.database.ReferenceDataSet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class TestService {

    public static byte[] convertToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static ListingDataSet getListingDataSetForTest() {
        Listing listingAllowed = new ListingBuilder("6022bade-659e-448a-a9fc-f588609f9b6b", "testTitle")
            .listingPrice(15.72)
            .listingStatus(4)
            .locationId("testLocationId")
            .marketplace(2)
            .currency("USD")
            .description("testDescription")
            .uploadTime(LocalDate.of(2018, 10, 2))
            .ownerEmailAddress("testEmail@email.com")
            .quantity(1)
            .createListing();

        Listing listingNotAllowed = new ListingBuilder("testId", null)     //UUID violation, null violation
            .listingPrice(15)                                                   //2 decimals violation
            .listingStatus(3)                                                   //reference violation
            .locationId("testLocationId2")                                      //reference violation
            .marketplace(7)                                                     //reference violation
            .currency("testCurrency")                                           //length violation
            .description(null)                                                  //null violation
            .uploadTime(LocalDate.of(2018, 10, 2))
            .ownerEmailAddress("testEmailemail.com")                            //email form violation
            .quantity(0)                                                        // > 0 violation
            .createListing();

        Status status = new Status();
        status.setId(4);
        Location location = new Location();
        location.setId("testLocationId");
        Marketplace marketplace = new Marketplace();
        marketplace.setId(2);

        ReferenceDataSet referenceDataSet = new ReferenceDataSet(Arrays.asList(status), Arrays.asList(location), Arrays.asList(marketplace));
        return new ListingDataSet(Arrays.asList(listingAllowed, listingNotAllowed), referenceDataSet);
    }
}
