package com.worldofbooks.listingsreport.output;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandlerJsonImpl implements FileHandlerJson {

    private Path localReportPath;

    public FileHandlerJsonImpl(Path localReportPath) {
        this.localReportPath = localReportPath;
    }

    @Override
    public void handleReportData(ReportDto reportDto) {
        try (Writer output = Files.newBufferedWriter(localReportPath, StandardCharsets.UTF_8);
             JsonWriter jsonWriter = new JsonWriter(output)) {

            Gson gson = new Gson();
            gson.toJson(gson.toJsonTree(reportDto), jsonWriter);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
