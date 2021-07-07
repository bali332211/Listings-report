package com.worldofbooks.listingsreport.output;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileHandlerJson {

    private Path localReportPath;

    public FileHandlerJson(Path localReportPath) {
        this.localReportPath = localReportPath;
    }

    public void handleReportData(ReportDto reportDto) {
        try (Writer output = Files.newBufferedWriter(localReportPath, StandardCharsets.UTF_8);
             JsonWriter jsonWriter = new JsonWriter(output)) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            JsonSerializer<ReportDto> serializer = getReportDtoSerializer();
            gsonBuilder.registerTypeAdapter(ReportDto.class, serializer);
            Gson customGson = gsonBuilder.create();

            customGson.toJson(customGson.toJsonTree(reportDto), jsonWriter);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private JsonSerializer<ReportDto> getReportDtoSerializer() {
        return (src, typeOfSrc, context) -> {
            JsonObject jsonDto = new JsonObject();
            jsonDto.addProperty("Total listing count", src.getListingCount());
            jsonDto.addProperty("Total ebay count", src.getTotalEbayListingCount());
            jsonDto.addProperty("Total ebay price", src.getTotalEbayListingPrice());
            jsonDto.addProperty("Average ebay price", src.getAverageEbayListingPrice());
            jsonDto.addProperty("Total amazon count", src.getTotalAmazonListingCount());
            jsonDto.addProperty("Total amazon price", src.getTotalAmazonListingPrice());
            jsonDto.addProperty("Average amazon price", src.getAverageAmazonListingPrice());
            jsonDto.addProperty("Best lister email", src.getBestListerEmail());
            JsonArray list = new JsonArray();
            List<MonthlyReport> monthlyReports = src.getMonthlyReports();
            Gson gson = new Gson();
            for (MonthlyReport monthlyReport : monthlyReports) {
                list.add(gson.toJsonTree(monthlyReport));
            }
            jsonDto.add("Monthly reports", list);
            return jsonDto;
        };
    }
}
