package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.database.validation.ViolationDataSet;

import java.util.List;

public interface ViolationProcessor {
    void processViolations(List<ViolationDataSet> violationDataSets);
}
