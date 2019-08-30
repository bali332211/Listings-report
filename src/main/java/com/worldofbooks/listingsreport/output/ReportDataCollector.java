package com.worldofbooks.listingsreport.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Component
public class ReportDataCollector implements ReportProcessor {

    private MarketplaceRepository marketplaceRepository;
    private FileHandlerJSON fileHandlerJSON;

    @Value(value = "${worldofbooks.ebay.name}")
    private String ebayName;
    @Value(value = "${worldofbooks.amazon.name}")
    private String amazonName;

    @Autowired
    public ReportDataCollector(MarketplaceRepository marketplaceRepository, FileHandlerJSON fileHandlerJSON) {
        this.marketplaceRepository = marketplaceRepository;
        this.fileHandlerJSON = fileHandlerJSON;
    }

    @Override
    public void collectReportData(List<Listing> listings) {
        Marketplace ebay = marketplaceRepository.findByMarketplaceName(ebayName);
        Marketplace amazon = marketplaceRepository.findByMarketplaceName(amazonName);
        int ebayId = ebay.getId();
        int amazonId = amazon.getId();
        int listingCount = listings.size();

        ReportDto reportDto = new ReportDto();
        MonthInReport[] monthsInReport = new MonthInReport[12];
        HashMap<String, Integer> emailCounts = new HashMap<>();

        reportDto.setListingCount(listingCount);

        for (Listing listing : listings) {
            if (listing.getUploadTime() != null) {
                int month = getMonthOfUploadTime(listing);
                updateMonthlyReports(month, listing, monthsInReport, ebayId, amazonId);
            }
            reportDto.updateMarketPlaceDataWithListing(listing, ebayId, amazonId);

            String email = listing.getOwnerEmailAddress();
            addEmailToHashMap(email, emailCounts);
        }

        reportDto.setAverages();
        for (MonthInReport monthInReport : monthsInReport) {
            if(monthInReport != null) {
                monthInReport.monthlyReport.setAverages();
            }
        }
        reportDto.setMonthsInReport(monthsInReport);

        fileHandlerJSON.handleReportData(reportDto);
    }

    private void addEmailToHashMap(String email, HashMap<String, Integer> map) {
        map.compute(email, (k, v) -> {
            if (v != null) {
                return v + 1;
            }
            return 1;
        });
    }

    private void updateMonthlyReports(int month, Listing listing, MonthInReport[] monthsInReport, int ebayId, int amazonId) {
        MonthInReport monthInReport = monthsInReport[month - 1];
        if (monthInReport == null) {
            monthInReport = new MonthInReport(new MonthlyReport());
        }
        MonthlyReport monthlyReport = monthInReport.monthlyReport;

        monthlyReport.updateMarketPlaceDataWithListing(listing, ebayId, amazonId);
    }

    private int getMonthOfUploadTime(Listing listing) {
        LocalDate localDateUploadTime = listing.getUploadTime();
        return localDateUploadTime.getMonth().getValue();
    }

    public static final class ReportDto extends MarketplaceData {
        @SerializedName(value = "Total listing count")
        private int listingCount = 0;
        @SerializedName(value = "Monthly reports")
        private MonthInReport[] monthsInReport = new MonthInReport[12];

        public int getListingCount() {
            return listingCount;
        }

        public void setListingCount(int listingCount) {
            this.listingCount = listingCount;
        }

        public MonthInReport[] getMonthsInReport() {
            return monthsInReport;
        }

        public void setMonthsInReport(MonthInReport[] monthsInReport) {
            this.monthsInReport = monthsInReport;
        }
    }

    private static class MarketplaceData {
        @SerializedName(value = "Total eBay listing count")
        private int totalEbayListingCount = 0;
        @SerializedName(value = "Total eBay listing price")
        private long totalEbayListingPrice = 0;
        @SerializedName(value = "Total Amazon listing count")
        private int totalAmazonListingCount = 0;
        @SerializedName(value = "Average eBay listing price")
        private long averageEbayListingPrice = 0;
        @SerializedName(value = "Total Amazon listing price")
        private long totalAmazonListingPrice = 0;
        @SerializedName(value = "Average Amazon listing price")
        private long averageAmazonListingPrice = 0;
        @SerializedName(value = "Best lister email address")
        private String bestListerEmail = "";

        public MarketplaceData() {
        }

        public void updateMarketPlaceDataWithListing(Listing listing, int ebayId, int amazonId) {
            int marketplaceId = listing.getMarketplace();
            long listingPrice = listing.getListingPrice();

            if (marketplaceId == ebayId) {
                incrementEbayListingCount();
                addPriceToEbayListingPrice(listingPrice);
            } else if (marketplaceId == amazonId) {
                incrementAmazonListingCount();
                addPriceToAmazonListingPrice(listingPrice);
            }
        }

        private void incrementEbayListingCount() {
            totalEbayListingCount++;
        }

        private void addPriceToEbayListingPrice(long price) {
            totalEbayListingPrice += price;
        }

        private void incrementAmazonListingCount() {
            totalAmazonListingCount++;
        }

        private void addPriceToAmazonListingPrice(long price) {
            totalAmazonListingPrice += price;
        }

        public void setAverages() {
            averageEbayListingPrice = totalEbayListingCount != 0 ? totalEbayListingPrice / totalEbayListingCount : 0;
            averageAmazonListingPrice = totalAmazonListingCount != 0 ? totalAmazonListingPrice / totalAmazonListingCount : 0;
        }

        public void setBestListerEmail(String email) {
            bestListerEmail = email;
        }
    }

    private static final class MonthInReport {
        @JsonProperty("month_name")
        private MonthlyReport monthlyReport;

        public MonthInReport(MonthlyReport monthlyReport) {
            this.monthlyReport = monthlyReport;
        }
    }

    private static final class MonthlyReport extends MarketplaceData {

    }


}
