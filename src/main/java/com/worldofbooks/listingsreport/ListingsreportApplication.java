package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.database.DatabaseService;
import com.worldofbooks.listingsreport.output.ReportDataCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListingsreportApplication implements CommandLineRunner  {

	@Autowired
	private ReportDataCollector reportDataCollector;
	@Autowired
	private DatabaseService databaseService;

	public static void main(String[] args) {
		SpringApplication.run(ListingsreportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		databaseService.initDatabase();
	}
}
