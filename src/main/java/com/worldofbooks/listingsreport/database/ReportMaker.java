package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.database.validation.ListingValidationResult;
import com.worldofbooks.listingsreport.database.validation.ListingValidator;
import com.worldofbooks.listingsreport.api.*;
import com.worldofbooks.listingsreport.database.validation.ViolationDataSet;
import com.worldofbooks.listingsreport.output.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Component
public class ReportMaker {

    private DatabaseHandler databaseHandler;
    private ListingRepository listingRepository;
    private ApiHandler apiHandler;
    private ListingValidator listingValidator;
    private ReportProcessor reportProcessor;

    @Autowired
    public ReportMaker(DatabaseHandler databaseHandler, ListingRepository listingRepository,
                       ApiHandler apiHandler, ListingValidator listingValidator, ReportProcessor reportProcessor) {
        this.databaseHandler = databaseHandler;
        this.listingRepository = listingRepository;
        this.apiHandler = apiHandler;
        this.listingValidator = listingValidator;
        this.reportProcessor = reportProcessor;
    }

    @Transactional
    public void generateListingReport() throws IOException {
        ListingDataSet listingDataSet = apiHandler.getListingDataSetFromApi();
        databaseHandler.saveReferences(listingDataSet.getReferenceDataSet());

        ListingValidationResult listingValidationResult = listingValidator.validateListings(listingDataSet);

        List<Listing> validatedListings = listingValidationResult.getValidatedListings();
        List<ViolationDataSet> violationDataSets = listingValidationResult.getViolationDataSets();


        try (ViolationWriterCsv violationWriterCsv = new ViolationWriterCsv()) {
            
            violationDataSets.forEach(violationDataSet -> {
                Set<ConstraintViolation<Listing>> violations = violationDataSet.getViolations();
                List<String> referenceViolations = violationDataSet.getReferenceViolations();
                Listing listing = violationDataSet.getListing();
                violationWriterCsv.processViolations(violations, referenceViolations, listing);
            });

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }



        databaseHandler.saveEntities(validatedListings, listingRepository);

        ReportDto reportDto = reportProcessor.collectReportData(validatedListings);

        try (FtpClient ftpClient = new FtpClient("localhost", 21, "bali", "password")) {
            ftpClient.open();

            File file = new File("report.json");
            ftpClient.putFileToPath(file, "/report.json");
        }

        try {Path path = Files.createTempFile("report", "json");

            FileHandlerJson fileHandlerJson = new FileHandlerJsonImpl(path);
            fileHandlerJson.handleReportData(reportDto);

            Files.delete(path);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
