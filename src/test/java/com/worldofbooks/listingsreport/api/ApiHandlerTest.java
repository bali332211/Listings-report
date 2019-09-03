package com.worldofbooks.listingsreport.api;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.TestService;
import com.worldofbooks.listingsreport.database.ListingDataSet;
import com.worldofbooks.listingsreport.database.ReferenceDataSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ApiHandlerTest {

    private MockRestServiceServer mockServer;

    private ApiHandler apiHandler;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        apiHandler = new ApiHandler(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getEntitiesFromAPI() throws IOException {
        Listing listing = new ListingBuilder("testId", "testTitle")
            .listingPrice(15)
            .listingStatus(4)
            .currency("testCurrency")
            .description("testDescription")
            .locationId("testLocationId")
            .marketplace(7)
            .ownerEmailAddress("testEmail")
            .quantity(1)
            .createListing();

        Status status = new Status();
        status.setId(4);
        Location location = new Location();
        location.setId("testLocationId");
        Marketplace marketplace = new Marketplace();
        marketplace.setId(2);

        String urlStatus = "https://my.api.mockaroo.com/listingStatus?key=";
        String urlLocation = "https://my.api.mockaroo.com/location?key=";
        String urlMarketplace = "https://my.api.mockaroo.com/marketplace?key=";
        String urlListing = "https://my.api.mockaroo.com/listing?key=";

        mockServerExpectRequestTo(urlStatus, Collections.singletonList(status));
        mockServerExpectRequestTo(urlLocation, Collections.singletonList(location));
        mockServerExpectRequestTo(urlMarketplace, Collections.singletonList(marketplace));
        mockServerExpectRequestTo(urlListing, Collections.singletonList(listing));

        ListingDataSet listingDataSet = apiHandler.getListingDataSetFromApi();
        mockServer.verify();

        List<Listing> listingsFromApi = listingDataSet.getListings();
        Listing listingFromApi = listingsFromApi.get(0);
        assertThat(listingFromApi.getId(), is("testId"));
        assertThat(listingFromApi.getDescription(), is("testDescription"));
        assertThat(listingFromApi.getOwnerEmailAddress(), is("testEmail"));
        ReferenceDataSet referenceDataSet = listingDataSet.getReferenceDataSet();
        Location locationFromReferences = referenceDataSet.getLocations().get(0);
        assertThat(locationFromReferences.getId(), is("testLocationId"));
    }

    private void mockServerExpectRequestTo(String url, List<?> responseList) throws IOException{
        mockServer.expect(requestTo(url))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(TestService.convertToJson(responseList), MediaType.APPLICATION_JSON));
    }


    @Configuration
    public static class Config {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplateBuilder().build();
        }
    }

}
