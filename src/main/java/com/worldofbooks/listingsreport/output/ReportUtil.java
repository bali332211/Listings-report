package com.worldofbooks.listingsreport.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ReportUtil implements ReportProcessor {

    private MarketplaceRepository marketplaceRepository;

    @Value(value = "${worldofbooks.ebay.name}")
    private String ebayName;
    @Value(value = "${worldofbooks.amazon.name}")
    private String amazonName;

    @Autowired
    public ReportUtil(MarketplaceRepository marketplaceRepository) {
        this.marketplaceRepository = marketplaceRepository;
    }

    @Override
    public void collectReportData(List<Listing> listings) {
        Marketplace ebay = marketplaceRepository.findByMarketplaceName(ebayName);
        Marketplace amazon = marketplaceRepository.findByMarketplaceName(amazonName);
        int ebayId = ebay.getId();
        int amazonId = amazon.getId();
        int listingCount = listings.size();

        ReportDto reportDto = new ReportDto(ebayId, amazonId);
        MonthInReport[] monthsInReport = new MonthInReport[12];
        HashMap<String, Integer> emailCounts = new HashMap<>();

        reportDto.setListingCount(listingCount);

        for (Listing listing : listings) {
            if (listing.getUploadTime() != null) {
                int month = getMonthOfUploadTime(listing);
                updateMonthlyReports(month, listing, monthsInReport, ebayId, amazonId);
            }
            reportDto.updateMarketPlaceDataWithListing(listing);

            String email = listing.getOwnerEmailAddress();
            addEmailToHashMap(email, emailCounts);
        }

        reportDto.setAverages();
        for (MonthInReport monthInReport : monthsInReport) {
            monthInReport.monthlyReport.setAverages();
        }
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
            monthInReport = new MonthInReport(new MonthlyReport(ebayId, amazonId));
        }
        MonthlyReport monthlyReport = monthInReport.monthlyReport;

        monthlyReport.updateMarketPlaceDataWithListing(listing);
    }

    private int getMonthOfUploadTime(Listing listing) {
        LocalDate localDateUploadTime = listing.getUploadTime();
        return localDateUploadTime.getMonth().getValue();
    }

    public static final class ReportDto extends MarketplaceData {
        private int listingCount = 0;
        private List<MonthInReport> monthsInReport = new ArrayList<>();

        public ReportDto(int ebayId, int amazonId) {
            super(ebayId, amazonId);
        }

        public int getListingCount() {
            return listingCount;
        }

        public void setListingCount(int listingCount) {
            this.listingCount = listingCount;
        }
    }

    private static class MarketplaceData {
        private int totalEbayListingCount = 0;
        private int totalAmazonListingCount = 0;
        private long totalEbayListingPrice = 0;
        private long totalAmazonListingPrice = 0;
        private long averageEbayListingPrice = 0;
        private long averageAmazonListingPrice = 0;
        private int ebayId;
        private int amazonId;
        private String bestListerEmail = "";

        public MarketplaceData() {
        }

        public MarketplaceData(int ebayId, int amazonId) {
            this.ebayId = ebayId;
            this.amazonId = amazonId;
        }

        public void updateMarketPlaceDataWithListing(Listing listing) {
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

        public MonthlyReport(int ebayId, int amazonId) {
            super(ebayId, amazonId);
        }
    }


}
