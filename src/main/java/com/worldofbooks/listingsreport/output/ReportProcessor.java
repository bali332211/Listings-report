package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public interface ReportProcessor {

    ReportDto collectReportData(List<Listing> listings);
}
