package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportUtil {

    private DatabaseService databaseService;

    @Autowired
    public ReportUtil(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void collectReportData(Listing listing) {
        int listingCount = 0;
        int totalEbayListingCount = 0;
        int totalAmazonListingCount = 0;
        long totalEbayListingPrice = 0;
        long totalAmazonListingPrice = 0;


        long averageEbayListingPrice = totalEbayListingCount != 0 ? totalEbayListingPrice / totalEbayListingCount : 0;
        long averageAmazonListingPrice = 0;

    }

    private static final class ReportDto {
        private int listingCount = 0;
        private int totalEbayListingCount = 0;
        private int totalAmazonListingCount = 0;
        private long totalEbayListingPrice = 0;
        private long totalAmazonListingPrice = 0;


    }
}
