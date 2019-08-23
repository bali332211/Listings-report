package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.database.DatabaseService;
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
	private ReportUtil reportUtil;
	@Autowired
	private DatabaseService databaseService;

	public static void main(String[] args) {
		SpringApplication.run(ListingsreportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		databaseService.initStatuses();
	}
}
