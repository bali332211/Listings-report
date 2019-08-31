package com.worldofbooks.listingsreport.api;

import com.worldofbooks.listingsreport.TestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
    private ApiHandler apiHandler;
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
        apiHandler = new ApiHandler(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getEntitiesFromAPI() throws IOException {
        Listing listing = new Listing();
        listing.setId("idvalue");
        listing.setCurrency("1");
        listing.setDescription("1");
        listing.setListingPrice(1);
        listing.setListingStatus(1);
        listing.setLocationId("sadf");
        listing.setMarketplace(1);
        listing.setOwnerEmailAddress("dsf");
        listing.setTitle("dsf");
        listing.setQuantity(1);
        listing.setMarketplace(1);

        String url = "https://my.api.mockaroo.com/listing?key=63304c70";

        mockServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(TestService.convertToJson(Arrays.asList(listing)), MediaType.APPLICATION_JSON));

        List<Listing> result = apiHandler.getEntitiesFromAPI(url, Listing.class);

        mockServer.verify();
        assertThat(result.get(0).getId(), is("idvalue"));
    }

}
