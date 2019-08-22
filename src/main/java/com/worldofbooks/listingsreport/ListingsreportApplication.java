package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.database.ListingReporter;
import com.worldofbooks.listingsreport.database.ListingRepository;
import com.worldofbooks.listingsreport.retrievedata.ApiHandler;
import com.worldofbooks.listingsreport.retrievedata.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class ListingsreportApplication implements CommandLineRunner  {

	@Autowired
	private ApiHandler apiHandler;
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	@Autowired
	private ListingReporter listingReporter;

	public static void main(String[] args) {
		SpringApplication.run(ListingsreportApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = apiHandler.restTemplate(restTemplateBuilder);
		List<Listing> listings = apiHandler.getListings(restTemplate);

		listingReporter.saveListings(listings);
	}
}
