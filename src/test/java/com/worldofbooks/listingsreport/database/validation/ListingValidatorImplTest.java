package com.worldofbooks.listingsreport.database.validation;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.database.ListingDataSet;
import com.worldofbooks.listingsreport.database.ReferenceDataSet;
import com.worldofbooks.listingsreport.output.ReportProcessor;
import com.worldofbooks.listingsreport.output.ViolationWriterCsv;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    @Captor
    private ArgumentCaptor<ArrayList<Listing>> listingsCaptor;
    @Captor
    private ArgumentCaptor<Set<ConstraintViolation<Listing>>> violationsArgument;
    @Captor
    private ArgumentCaptor<ArrayList<String>> referenceViolationsArgument;

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

        List<Listing> validatedListings = listingValidator.validateListings(listingDataSet, violationWriterCsv);

        assertThat(validatedListings.size(), is(1));

        ArgumentCaptor<Listing> listingArgument = ArgumentCaptor.forClass(Listing.class);
        verify(violationWriterCsv, times(1))
                .processViolations(violationsArgument.capture(), referenceViolationsArgument.capture(), listingArgument.capture());

        Set<ConstraintViolation<Listing>> violationsArgumentValue = violationsArgument.getValue();
        assertThat(violationsArgumentValue.size(), Matchers.is(7));
        List<String> referenceViolationsArgumentValue = referenceViolationsArgument.getValue();
        assertThat(referenceViolationsArgumentValue.size(), Matchers.is(3));
        Listing listingArgumentValue = listingArgument.getValue();
        assertThat(listingArgumentValue.getId(), Matchers.is("testId"));
        assertThat(listingArgumentValue.getListingPrice(), Matchers.is(15D));
        verifyNoMoreInteractions(violationWriterCsv);

        verify(reportProcessor, times(1))
                .collectReportData(listingsCaptor.capture());

        List<Listing> listingsCaptorValue = listingsCaptor.getValue();
        Listing validatedListing = listingsCaptorValue.get(0);
        assertThat(validatedListing.getId(), Matchers.is("6022bade-659e-448a-a9fc-f588609f9b6b"));
        assertThat(validatedListing.getListingPrice(), Matchers.is(15.01D));
        verifyNoMoreInteractions(reportProcessor);
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
