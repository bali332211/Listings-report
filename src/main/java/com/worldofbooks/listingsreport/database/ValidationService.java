package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.ReportUtil;
import com.worldofbooks.listingsreport.api.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
public class ValidationService {

    private Validator validator;
    private ReportUtil reportUtil;

    @Autowired
    public ValidationService(Validator validator, ReportUtil reportUtil) {
        this.validator = validator;
        this.reportUtil = reportUtil;
    }

    public List<Listing> validateListings(List<Listing> listings) {
        List<Listing> validatedListings = new ArrayList<>();

        listings.forEach(listing -> {
            Set<ConstraintViolation<Listing>> violations = validator.validate(listing);

            if (violations.isEmpty()) {
                validatedListings.add(listing);
                reportUtil.collectReportData(listing);
            } else {
                writeViolationsToCSV(violations, listing);
            }
        });


        return validatedListings;
    }

    private void writeViolationsToCSV(Set<ConstraintViolation<Listing>> violations, Listing listing) {
        List<ViolationDto> violationDtos = getViolationDtosForCSV(violations, listing);

    }

    private List<ViolationDto> getViolationDtosForCSV(Set<ConstraintViolation<Listing>> violations, Listing listing) {
        List<ViolationDto> violationDtos = new ArrayList<>();

        violations.forEach(violation -> {
            ViolationDto violationDto = new ViolationDto(
                listing.getId(),
                listing.getMarketplace(),
                violation.getPropertyPath().toString());

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
