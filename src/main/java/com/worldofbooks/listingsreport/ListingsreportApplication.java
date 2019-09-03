package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.output.ReportMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListingsreportApplication implements CommandLineRunner {

    @Autowired
    private ReportMaker reportMaker;

    public static void main(String[] args) {
        SpringApplication.run(ListingsreportApplication.class, args);
    }

    @Override
    public void run(String... args) {

        if (args.length >= 2) {
            reportMaker.generateListingReport(args[0], args[1]);
            System.out.println("Report generated");
        } else {
			System.out.println("Missing parameter(s):");
			System.out.println("local_report_path");
			System.out.println("ftp_report_path");
		}

    }
}
