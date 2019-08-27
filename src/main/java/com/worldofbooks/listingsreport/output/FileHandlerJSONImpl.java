package com.worldofbooks.listingsreport.output;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import com.worldofbooks.listingsreport.output.ReportUtil.ReportDto;

import java.io.FileWriter;

@Component
public class FileHandlerJSONImpl implements FileHandlerJSON {

    private String fileName = "report.json";

    @Override
    public void writeDtoToFile(ReportDto reportDto)  throws Exception {
        FileWriter file = new FileWriter(fileName);
        file.write(new Gson().toJson(reportDto));
        file.flush();
    }
}
