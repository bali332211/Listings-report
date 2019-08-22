package com.worldofbooks.listingsreport.retrievedata;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApiHandler {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public List<Listing> getListings(RestTemplate restTemplate) {
        ResponseEntity<List<Listing>> response = restTemplate.exchange(
            "https://my.api.mockaroo.com/listing?key=63304c70",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Listing>>() {
            });
        List<Listing> listings = response.getBody();
        return listings;

//        return args -> {
//            Quote quote = restTemplate.getForObject(
//                "https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//            log.info(quote.toString());
//        };

    }
}
