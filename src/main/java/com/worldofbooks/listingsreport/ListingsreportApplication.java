package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		databaseService.initDatabase();
	}
}
