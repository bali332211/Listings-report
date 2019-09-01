package com.worldofbooks.listingsreport.database.validation;


import com.fasterxml.jackson.databind.DeserializationContext;
import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.ListingsreportApplication;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.database.ListingDataSet;
import com.worldofbooks.listingsreport.database.ReferenceDataSet;
import com.worldofbooks.listingsreport.output.ReportProcessor;
import com.worldofbooks.listingsreport.output.ViolationWriterCsv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ListingsreportApplication.class)
public class ListingValidatorImplTest {

    @Autowired
    private ListingValidator listingValidator;
    @Autowired
    private ViolationWriterCsv violationWriterCsv;

    @Autowired
    private Validator validator;
    @Autowired
    private ReportProcessor reportProcessor;

    @Test
    public void validateListings() {
        Listing listingAllowed = new ListingBuilder("testId", "testTitle")
                .listingPrice(15)
                .listingStatus(4)
                .currency("testCurrency")
                .description("testDescription")
                .locationId("testLocationId")
                .marketplace(2)
                .uploadTime(LocalDate.of(2018, 10, 2))
                .ownerEmailAddress("testEmail")
                .quantity(1)
                .createListing();

        Listing listingNotAllowed = new ListingBuilder("testId", "testTitle")
                .listingPrice(15)
                .listingStatus(4)
                .currency("testCurrency")
                .description("testDescription")
                .locationId("testLocationId")
                .marketplace(7)
                .uploadTime(LocalDate.of(2018, 10, 2))
                .ownerEmailAddress("testEmail")
                .quantity(1)
                .createListing();

        Status status = new Status();
        status.setId(4);
        Location location = new Location();
        location.setId("testLocationId");
        Marketplace marketplace = new Marketplace();
        marketplace.setId(2);

        ReferenceDataSet referenceDataSet = new ReferenceDataSet(Arrays.asList(status), Arrays.asList(location), Arrays.asList(marketplace));
        ListingDataSet listingDataSet = new ListingDataSet(Arrays.asList(listingAllowed, listingNotAllowed), referenceDataSet);

        List<Listing> validatedListings = listingValidator.validateListings(listingDataSet, violationWriterCsv);

        assertThat(validatedListings.size(), is(1));

    }

    @Configuration
    public static class Config {

        @Bean(name = "TestViolationWriterCsvConfiguration")
        @Primary
        public ViolationWriterCsv violationWriterCsv() {
            return Mockito.mock(ViolationWriterCsv.class);
        }

        @Bean(name = "TestReportProcessorConfiguration")
        @Primary
        public ReportProcessor reportProcessor() {
            return Mockito.mock(ReportProcessor.class);
        }

        @Bean(name = "TestValidatorConfiguration")
        @Primary
        public Validator validator() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            return factory.getValidator();
        }
    }


}
