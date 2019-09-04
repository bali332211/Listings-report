package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.output.ReportMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ListingsreportApplication implements CommandLineRunner {

    @Autowired
    private ReportMaker reportMaker;

    public static void main(String[] args) {
        SpringApplication.run(ListingsreportApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length >= 3) {

            Path importLogPath = Paths.get(args[0]);
            Path localReportPath = Paths.get(args[1]);
            reportMaker.generateListingReport(importLogPath, localReportPath, args[2]);
            System.out.println("Report generated");
        } else {
            System.out.println("Missing parameter(s):");
            System.out.println("import_log_path");
            System.out.println("local_report_path");
            System.out.println("ftp_report_path");
        }

    }
}
