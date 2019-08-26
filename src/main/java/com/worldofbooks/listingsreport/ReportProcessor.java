package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.api.Listing;

import java.util.List;

public interface ReportProcessor {

    void collectReportData(List<Listing> listings);
}
