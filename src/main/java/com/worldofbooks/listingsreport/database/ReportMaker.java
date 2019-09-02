package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.database.validation.ListingValidator;
import com.worldofbooks.listingsreport.api.*;
import com.worldofbooks.listingsreport.output.FtpClient;
import com.worldofbooks.listingsreport.output.ViolationWriterCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Component
public class ReportMaker {

    private DatabaseHandler databaseHandler;
    private ListingRepository listingRepository;
    private ApiHandler apiHandler;
    private ListingValidator listingValidator;

    @Autowired
    public ReportMaker(DatabaseHandler databaseHandler, ListingRepository listingRepository,
                       ApiHandler apiHandler, ListingValidator listingValidator) {
        this.databaseHandler = databaseHandler;
        this.listingRepository = listingRepository;
        this.apiHandler = apiHandler;
        this.listingValidator = listingValidator;
    }

    @Transactional
    public void generateListingReport() throws IOException {
        ListingDataSet listingDataSet = apiHandler.getListingDataSetFromApi();
        databaseHandler.saveReferences(listingDataSet.getReferenceDataSet());

        List<Listing> validatedListings;
        try (ViolationWriterCsv violationWriterCsv = new ViolationWriterCsv()) {
            validatedListings = listingValidator.validateListings(listingDataSet, violationWriterCsv);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        databaseHandler.saveEntities(validatedListings, listingRepository);

        try (FtpClient ftpClient = new FtpClient("localhost", 21, "bali", "password")) {
            ftpClient.open();

            File file = new File("report.json");
            ftpClient.putFileToPath(file, "/report.json");
        }
    }

}
