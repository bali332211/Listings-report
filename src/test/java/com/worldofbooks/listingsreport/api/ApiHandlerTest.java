package com.worldofbooks.listingsreport.api;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.TestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiHandlerTest {

    private MockRestServiceServer mockServer;

    @Autowired
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

        String url = "https://my.api.mockaroo.com/listing?key=63304c70";

        mockServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(TestService.convertToJson(Arrays.asList(listing)), MediaType.APPLICATION_JSON));

        List<Listing> listings = apiHandler.getEntitiesFromAPI(url, Listing.class);

        mockServer.verify();
        Listing listingFromApi = listings.get(0);
        assertThat(listingFromApi.getId(), is("testId"));
        assertThat(listingFromApi.getDescription(), is("testDescription"));
        assertThat(listingFromApi.getOwnerEmailAddress(), is("testEmail"));
    }
}
