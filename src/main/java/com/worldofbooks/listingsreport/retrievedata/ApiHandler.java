package com.worldofbooks.listingsreport.retrievedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ApiHandler {

    private RestTemplateBuilder builder;

    @Autowired
    public ApiHandler(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

    public <T> List<T> getEntitiesFromAPI(RestTemplate restTemplate, String url) {
        ResponseEntity<List<T>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<T>>() {
            });
        List<T> entities = response.getBody();
        return entities;
    }

//    @Bean
//    public List<Listing> getListings(RestTemplate restTemplate) {
//        ResponseEntity<List<Listing>> response = restTemplate.exchange(
//            "https://my.api.mockaroo.com/listing?key=63304c70",
//            HttpMethod.GET,
//            null,
//            new ParameterizedTypeReference<List<Listing>>() {
//            });
//        List<Listing> listings = response.getBody();
//        return listings;
//
////        return args -> {
////            Quote quote = restTemplate.getForObject(
////                "https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
////            log.info(quote.toString());
////        };
//
//    }
//
    @Bean
    public List<Status> getStatuses(RestTemplate restTemplate) {
        ResponseEntity<List<Status>> response = restTemplate.exchange(
            "https://my.api.mockaroo.com/listingStatus?key=63304c70",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Status>>() {
            });
        List<Status> statuses = response.getBody();
        return statuses;
    }



}
