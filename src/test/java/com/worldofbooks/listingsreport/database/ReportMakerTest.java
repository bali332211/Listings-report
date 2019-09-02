//package com.worldofbooks.listingsreport.database;
//
//import com.worldofbooks.listingsreport.ListingBuilder;
//import com.worldofbooks.listingsreport.api.*;
//import com.worldofbooks.listingsreport.database.validation.ListingValidator;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.internal.verification.VerificationModeFactory.times;
//
//@RunWith(SpringRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//public class ReportMakerTest {
//
//    private ReportMaker reportMaker;
//
//    @Autowired
//    private ApiHandler apiHandler;
//    @Autowired
//    private ListingValidator listingValidator;
//    @Autowired
//    private ListingRepository listingRepository;
//    @Autowired
//    private StatusRepository statusRepository;
//    @Autowired
//    private LocationRepository locationRepository;
//    @Autowired
//    private MarketplaceRepository marketplaceRepository;
//
//
//    @Test
//    public void initListingDatabase() {
//        Status status = new Status();
//        status.setId(4);
//        Location location = new Location();
//        location.setId("testLocationId");
//        Marketplace marketplace = new Marketplace();
//        marketplace.setId(2);
//
//        Listing listingAllowed = new ListingBuilder("6022bade-659e-448a-a9fc-f588609f9b6b", "testTitle")
//            .listingPrice(15.72)
//            .listingStatus(4)
//            .locationId("testLocationId")
//            .marketplace(2)
//            .currency("USD")
//            .description("testDescription")
//            .uploadTime(LocalDate.of(2018, 10, 2))
//            .ownerEmailAddress("testEmail@email.com")
//            .quantity(1)
//            .createListing();
//
//        List<Listing> listings = Arrays.asList(listingAllowed);
//
//        when(apiHandler.getEntitiesFromAPI(anyString(), Status.class)).thenReturn(Arrays.asList(status));
//        when(apiHandler.getEntitiesFromAPI(anyString(), Location.class)).thenReturn(Arrays.asList(location));
//        when(apiHandler.getEntitiesFromAPI(anyString(), Marketplace.class)).thenReturn(Arrays.asList(marketplace));
//        when(apiHandler.getEntitiesFromAPI(anyString(), Listing.class)).thenReturn(listings);
//
//        reportMaker.generateListingReport();
//
//        verify(apiHandler, times(4))
//            .getEntitiesFromAPI(anyString(), any());
//    }
//
//
//    @Configuration
//    public static class Config {
//
//        @Bean(name = "TestApiHandlerConfiguration")
//        @Primary
//        public ApiHandler apiHandler() {
//            return Mockito.mock(ApiHandler.class);
//        }
//
//        @Bean(name = "TestListingValidatorConfiguration")
//        @Primary
//        public ListingValidator listingValidator() {
//            return Mockito.mock(ListingValidator.class);
//        }
//
//        @Bean(name = "TestListingRepositoryConfiguration")
//        @Primary
//        public ListingRepository listingRepository() {
//            return Mockito.mock(ListingRepository.class);
//        }
//
//        @Bean(name = "TestStatusRepositoryConfiguration")
//        @Primary
//        public StatusRepository statusRepository() {
//            return Mockito.mock(StatusRepository.class);
//        }
//
//        @Bean(name = "TestLocationRepositoryConfiguration")
//        @Primary
//        public LocationRepository locationRepository() {
//            return Mockito.mock(LocationRepository.class);
//        }
//
//        @Bean(name = "TestMarketplaceRepositoryConfiguration")
//        @Primary
//        public MarketplaceRepository marketplaceRepository() {
//            return Mockito.mock(MarketplaceRepository.class);
//        }
//
//    }
//
//}
