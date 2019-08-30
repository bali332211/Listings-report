package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.output.ReportDataCollector.ReportDto;

public interface FileHandlerJSON {
    void handleReportData(ReportDto reportDto);
}
