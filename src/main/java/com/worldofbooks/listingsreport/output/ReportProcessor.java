package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public interface ReportProcessor {

    void collectReportData(List<Listing> listings);
}
