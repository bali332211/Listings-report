package com.worldofbooks.listingsreport.output;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class OutputProcessorFactory {

    public FileHandlerJson getFileHandlerJson(Path localReportPath) {
        return new FileHandlerJsonImpl(localReportPath);
    }

    public FtpClient getFtpClient(String ftpServer, String ftpPort, String ftpUser, String ftpPassword) {
        int ftpPortNumber;
        try {
            ftpPortNumber = Integer.parseInt(ftpPort);
        } catch (NumberFormatException e) {
            ftpPortNumber = 0;
        }
        return new FtpClient(ftpServer, ftpPortNumber, ftpUser, ftpPassword);
    }

    public ViolationWriterCsv getViolationWriterCsv(Path importLogPath) throws IOException {
        return new ViolationWriterCsv(importLogPath);
    }


}
