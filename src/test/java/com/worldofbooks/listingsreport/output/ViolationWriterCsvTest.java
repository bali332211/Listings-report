package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.database.validation.ViolationDataSet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.validation.*;
import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ViolationWriterCsvTest {

    @ClassRule
    public static final TemporaryFolder TEMPORARY_FOLDER = new TemporaryFolder();

    @Autowired
    private Validator validator;

    @Test
    public void writeCsv() throws IOException {
        Listing listingNotAllowed = new ListingBuilder("testId", null)     //UUID violation, null violation
            .listingPrice(15)                                                       //2 decimals violation
            .listingStatus(3)                                                       //reference violation
            .locationId("testLocationId2")                                          //reference violation
            .marketplace(7)                                                         //reference violation
            .currency("testCurrency")                                               //length violation
            .description(null)                                                      //null violation
            .uploadTime(LocalDate.of(2018, 10, 2))
            .ownerEmailAddress("testEmailemail.com")                                //email form violation
            .quantity(0)                                                            // > 0 violation
            .createListing();

        Set<ConstraintViolation<Listing>> violations = validator.validate(listingNotAllowed);
        List<String> referenceViolations = Arrays.asList("listingStatus", "locationId", "marketplace");
        ViolationDataSet violationDataSet = new ViolationDataSet(listingNotAllowed, violations, referenceViolations);

        Path testLogPath = TEMPORARY_FOLDER.newFile("testLog.csv").toPath();
        try (ViolationWriterCsv violationWriterCsv = new ViolationWriterCsv(testLogPath)) {
            violationWriterCsv.processViolations(Collections.singletonList(violationDataSet));
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(testLogPath.toString()))) {
            CSVParser csvRecords = new CSVParser(reader, CSVFormat.DEFAULT);
            List<CSVRecord> records = csvRecords.getRecords();
            assertThat(records.size(), is(11));
        }
    }

    @Configuration
    public static class Config {

        @Bean
        public Validator validator() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            return factory.getValidator();
        }
    }

}
