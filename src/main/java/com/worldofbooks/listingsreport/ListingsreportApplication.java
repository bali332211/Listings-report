package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.retrievedata.ApiHandler;
import com.worldofbooks.listingsreport.retrievedata.Listing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class ListingsreportApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListingsreportApplication.class, args);

		ApiHandler apiHandler = new ApiHandler();
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		RestTemplate restTemplate = apiHandler.restTemplate(restTemplateBuilder);
		List<Listing> listings = apiHandler.getListings(restTemplate);
		System.out.println(listings);
	}


}
