package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.api.Listing;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CsvUtil implements CsvProcessor {
    @Override
    public void processViolations(Set<ConstraintViolation<Listing>> violations, List<String> referenceViolations, Listing listing) {
        List<ViolationDto> violationDtos = getViolationDtosForCSV(violations, referenceViolations, listing);

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
