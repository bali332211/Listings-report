package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.api.Listing;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

public interface ViolationProcessor {

    void processViolations(Set<ConstraintViolation<Listing>> violations, List<String> referenceViolations, Listing listing);
}
