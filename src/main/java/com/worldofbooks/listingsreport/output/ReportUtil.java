package com.worldofbooks.listingsreport.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReportUtil implements ReportProcessor{

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

        MarketplaceData marketplaceData = new MarketplaceData(ebayId, amazonId);
        ReportDto reportDto = new ReportDto();
        MonthInReport[] monthsInReport = new MonthInReport[12];

        reportDto.setListingCount(listingCount);

        for (Listing listing : listings) {
            int month = getMonthOfUploadTime(listing);
            updateMonthlyReports(month, listing, monthsInReport);
            marketplaceData.updateMarketPlaceDataWithListing(listing);
        }
        marketplaceData.setAverages();
    }

    private void updateMonthlyReports(int month, Listing listing, MonthInReport[] monthsInReport) {
        MonthInReport monthInReport = monthsInReport[month - 1];
        if (monthInReport == null) {
            monthInReport = new MonthInReport();
        }
        MonthlyReport monthlyReport = monthInReport.monthlyReport;

        monthlyReport.updateMarketPlaceDataWithListing(listing);
    }

    private int getMonthOfUploadTime(Listing listing) {
        LocalDate localDateUploadTime = listing.getUploadTime();
        return localDateUploadTime.getMonth().getValue();
    }

    private static final class ReportDto extends MarketplaceData {
        private int listingCount = 0;
        private List<MonthInReport> monthsInReport = new ArrayList<>();

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

        private void setAverages() {
            averageEbayListingPrice = totalEbayListingCount != 0 ? totalEbayListingPrice / totalEbayListingCount : 0;
            averageAmazonListingPrice = totalAmazonListingCount != 0 ? totalAmazonListingPrice / totalAmazonListingCount : 0;
        }
    }

    private static final class MonthInReport {
        @JsonProperty("month_name")
        private MonthlyReport monthlyReport = new MonthlyReport();
    }

    private static final class MonthlyReport extends MarketplaceData {

    }


}
