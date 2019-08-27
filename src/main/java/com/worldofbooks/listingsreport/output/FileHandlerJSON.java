package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.output.ReportUtil.ReportDto;

public interface FileHandlerJSON {
    void writeDtoToFile(ReportDto reportDto);
}
