package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import com.worldofbooks.listingsreport.database.validation.ListingValidator;
import com.worldofbooks.listingsreport.database.validation.ListingValidatorImpl;
import org.junit.Before;
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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReportDataCollectorTest {

    private ReportDataCollector reportDataCollector;

    @Autowired
    private FileHandlerJson fileHandlerJson;
    @Autowired
    private MarketplaceRepository marketplaceRepository;

    @Captor
    private ArgumentCaptor<ArrayList<Listing>> listingsCaptor;

    @Before
    public void setup() {
        reportDataCollector = new ReportDataCollector(marketplaceRepository, fileHandlerJson);
    }

    @Test
    public void collectReportData() {
        Marketplace marketplace = new Marketplace();
        marketplace.setMarketplaceName("marketplaceName");
        when(marketplaceRepository.findByMarketplaceName(anyString())).thenReturn(marketplace);

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

        reportDataCollector.collectReportData(Arrays.asList(listingAllowed));

        ReportDto reportDto = new ReportDto();

        verify(fileHandlerJson.handleReportData(), times(1))
                .collectReportData(listingsCaptor.capture());

        List<Listing> listingsCaptorValue = listingsCaptor.getValue();

    }


    @Configuration
    public static class Config {

        @Bean(name = "TestFileHandlerJsonConfiguration")
        @Primary
        public FileHandlerJson fileHandlerJson() {
            return Mockito.mock(FileHandlerJson.class);
        }

        @Bean(name = "TestMarketplaceRepositoryConfiguration")
        @Primary
        public MarketplaceRepository marketplaceRepository() {
            return Mockito.mock(MarketplaceRepository.class);
        }

    }

}
