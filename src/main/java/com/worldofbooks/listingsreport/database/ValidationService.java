package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Listing;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Service
public class ValidationService {

    public List<Listing> validateListing(List<Listing> listings) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        List<Listing> validatedListings = new ArrayList<>();

        for (Listing listing : listings) {
            Set<ConstraintViolation<Listing>> violations = validator.validate(listing);

            for (ConstraintViolation<Listing> violation : violations) {
                System.out.print("Property: ");
                System.out.print(violation.getPropertyPath());
                System.out.print(" value: ");
                System.out.print(violation.getInvalidValue());
                System.out.print(" error: ");
                System.out.print(violation.getMessage());
                System.out.print(" id: ");
                System.out.print(listing.getId());
                System.out.print(" marketplace: ");
                System.out.println(listing.getMarketplace());
            }

            if(violations.isEmpty()) {
                validatedListings.add(listing);
            }
        }
        return validatedListings;
    }

    public boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static final class InvalidListing {
        private final String listingId;
        private final String marketplaceName;
        private final String fieldName;

        public InvalidListing(String listingId, String marketplaceName, String fieldName) {
            this.listingId = listingId;
            this.marketplaceName = marketplaceName;
            this.fieldName = fieldName;
        }

        public String getListingId() {
            return listingId;
        }

        public String getMarketplaceName() {
            return marketplaceName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
