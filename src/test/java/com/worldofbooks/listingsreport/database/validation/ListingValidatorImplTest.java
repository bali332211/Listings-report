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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ListingValidatorImplTest {

    @Autowired
    private ListingValidator listingValidator;
    @Autowired
    private ViolationWriterCsv violationWriterCsv;

    @Autowired
    private Validator validator;
    @Autowired
    private ReportProcessor reportProcessor;

//    @Captor
//    private ArgumentCaptor<List<Listing>> listingsCaptor;

    @Test
    public void validateListings() {
        Listing listingAllowed = new ListingBuilder("6022bade-659e-448a-a9fc-f588609f9b6b", "testTitle")
                .listingPrice(15.01)
                .listingStatus(4)
                .locationId("testLocationId")
                .marketplace(2)
                .currency("USD")
                .description("testDescription")
                .uploadTime(LocalDate.of(2018, 10, 2))
                .ownerEmailAddress("testEmail@email.com")
                .quantity(1)
                .createListing();

        Listing listingNotAllowed = new ListingBuilder("testId", null) //UUID violation, null violation
                .listingPrice(15) //2 decimals violation
                .listingStatus(3) //reference violation
                .locationId("testLocationId2") //reference violation
                .marketplace(7) //reference violation
                .currency("testCurrency") //length violation
                .description(null) //null violation
                .uploadTime(LocalDate.of(2018, 10, 2))
                .ownerEmailAddress("testEmailemail.com") //email form violation
                .quantity(0) // > 0 violation
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

        verify(violationWriterCsv, times(1))
                .processViolations(HashSet.class, ArrayList.class, listingsCaptor.capture());

        Note noteArgumentValue = noteArgument.getValue();


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
        @Bean(name = "TestListingValidatorConfiguration")
        @Primary
        public ListingValidator listingValidator() {
            return new ListingValidatorImpl(validator(), reportProcessor());
        }
    }


}
