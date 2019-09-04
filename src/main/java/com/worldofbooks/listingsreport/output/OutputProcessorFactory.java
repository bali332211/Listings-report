package com.worldofbooks.listingsreport.output;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class OutputProcessorFactory {

    @Value(value = "${ftp.server}")
    private String ftpServer;
    @Value(value = "${ftp.port}")
    private String ftpPort;
    @Value(value = "${ftp.user}")
    private String ftpUser;
    @Value(value = "${ftp.password}")
    private String ftpPassword;

    public FileHandlerJson getFileHandlerJson(Path localReportPath) {
        return new FileHandlerJson(localReportPath);
    }

    public FtpClient getFtpClient() {
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
