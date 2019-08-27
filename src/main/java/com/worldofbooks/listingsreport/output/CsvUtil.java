package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.api.Listing;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class CsvUtil implements CsvProcessor {

    private static final String SAMPLE_CSV_FILE = "importLog.csv";

    @Override
    public void processViolations(Set<ConstraintViolation<Listing>> violations, List<String> referenceViolations, Listing listing) throws IOException {
        List<ViolationDto> violationDtos = getViolationDtosForCSV(violations, referenceViolations, listing);

        try (
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ListingId", "MarketplaceName", "InvalidField"));
        ) {
            for (ViolationDto violationDto : violationDtos) {
                csvPrinter.printRecord(violationDto.listingId, String.valueOf(violationDto.marketplaceName), violationDto.fieldName);
            }
            csvPrinter.flush();
        }
    }

    private List<ViolationDto> getViolationDtosForCSV(Set<ConstraintViolation<Listing>> violations, List<String> referenceViolations, Listing listing) {
        List<ViolationDto> violationDtos = new ArrayList<>();
        String id = listing.getId();
        int marketplace = listing.getMarketplace();

        violations.forEach(violation -> {
            ViolationDto violationDto = new ViolationDto(
                id,
                marketplace,
                violation.getPropertyPath().toString());

            violationDtos.add(violationDto);
        });

        referenceViolations.forEach(violation -> {
            ViolationDto violationDto = new ViolationDto(
                id,
                marketplace,
                violation);

            violationDtos.add(violationDto);
        });
        return violationDtos;
    }



    private static final class ViolationDto {
        private final String listingId;
        private final int marketplaceName;
        private final String fieldName;

        public ViolationDto(String listingId, int marketplaceName, String fieldName) {
            this.listingId = listingId;
            this.marketplaceName = marketplaceName;
            this.fieldName = fieldName;
        }

        public String getListingId() {
            return listingId;
        }

        public int getMarketplaceName() {
            return marketplaceName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
