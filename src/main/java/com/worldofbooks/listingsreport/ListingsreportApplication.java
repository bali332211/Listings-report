package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.database.ReportMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListingsreportApplication implements CommandLineRunner  {

	@Autowired
	private ReportMaker reportMaker;

	public static void main(String[] args) {
		SpringApplication.run(ListingsreportApplication.class, args);
	}

	@Override
	public void run(String... args) {
		reportMaker.generateListingReport(args[0], args[1]);
		System.out.println("Report generated");
	}
}
