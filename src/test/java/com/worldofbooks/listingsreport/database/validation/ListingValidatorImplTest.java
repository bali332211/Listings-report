package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.api.ListingDataSet;
import com.worldofbooks.listingsreport.database.ReferenceDataSet;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ListingValidatorImplTest {


    private ListingValidator listingValidator;

    @Autowired
    private Validator validator;

    @Before
    public void setup() {
        listingValidator = new ListingValidatorImpl(validator);
    }

    @Test
    public void validateListings() {
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
        ListingDataSet listingDataSet = new ListingDataSet(Arrays.asList(listingAllowed, listingNotAllowed), referenceDataSet);

        ListingValidationResult listingValidationResult = listingValidator.validateListings(listingDataSet);

        List<Listing> validatedListings = listingValidationResult.getValidatedListings();
        assertThat(validatedListings.size(), is(1));
        Listing validatedListing = validatedListings.get(0);
        assertThat(validatedListing.getId(), Matchers.is("6022bade-659e-448a-a9fc-f588609f9b6b"));
        assertThat(validatedListing.getListingPrice(), Matchers.is(15.72D));

        List<ViolationDataSet> violationDataSets = listingValidationResult.getViolationDataSets();
        assertThat(violationDataSets.size(), Matchers.is(1));
        ViolationDataSet violationDataSetFirst = violationDataSets.get(0);
        assertThat(violationDataSetFirst.getViolations().size(), Matchers.is(7));
        assertThat(violationDataSetFirst.getReferenceViolations().size(), Matchers.is(3));
        Listing listingViolating = violationDataSetFirst.getListing();
        assertThat(listingViolating.getId(), Matchers.is("testId"));
        assertThat(listingViolating.getListingPrice(), Matchers.is(15D));
    }

    @Configuration
    public static class Config {

        @Bean(name = "TestValidatorConfiguration")
        @Primary
        public Validator validator() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            return factory.getValidator();
        }

    }
}
