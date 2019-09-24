package com.worldofbooks.listingsreport.output;

import com.google.gson.annotations.SerializedName;
import com.worldofbooks.listingsreport.api.Listing;

import java.util.*;

public class ReportDto extends MarketplaceData {

    @SerializedName(value = "Total listing count")
    private int listingCount = 0;
    @SerializedName(value = "Monthly reports")
    private List<MonthlyReport> monthlyReports = new ArrayList<>();

    public int getListingCount() {
        return listingCount;
    }

    public void setListingCount(int listingCount) {
        this.listingCount = listingCount;
    }

    public List<MonthlyReport> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(List<MonthlyReport> monthlyReports) {
        this.monthlyReports = monthlyReports;
    }
}

class MarketplaceData {
    @SerializedName(value = "Total eBay listing count")
    private int totalEbayListingCount = 0;
    @SerializedName(value = "Total eBay listing price")
    private double totalEbayListingPrice = 0;
    @SerializedName(value = "Total Amazon listing count")
    private double totalAmazonListingCount = 0;
    @SerializedName(value = "Average eBay listing price")
    private double averageEbayListingPrice = 0;
    @SerializedName(value = "Total Amazon listing price")
    private double totalAmazonListingPrice = 0;
    @SerializedName(value = "Average Amazon listing price")
    private double averageAmazonListingPrice = 0;
    @SerializedName(value = "Best lister email address")
    private String bestListerEmail = "";

    public void updateMarketPlaceDataWithListing(List<Listing> listings, int ebayId, int amazonId) {
        for (Listing listing : listings) {
            int marketplaceId = listing.getMarketplace();
            double listingPrice = listing.getListingPrice();

            if (marketplaceId == ebayId) {
                incrementEbayListingCount();
                addPriceToEbayListingPrice(listingPrice);
            } else if (marketplaceId == amazonId) {
                incrementAmazonListingCount();
                addPriceToAmazonListingPrice(listingPrice);
            }
        }

        String bestLister = getBestListerEmailFromListings(listings);
        setBestListerEmail(bestLister);
        setAverages();
    }

    private String getBestListerEmailFromListings(List<Listing> listings) {
        HashMap<String, Integer> emailCounts = new HashMap<>();

        for (Listing listing : listings) {
            String email = listing.getOwnerEmailAddress();
            addEmailToEmailCounts(email, emailCounts);
        }

        return getBestListerEmailFromMap(emailCounts);
    }

    private String getBestListerEmailFromMap(HashMap<String, Integer> emailCounts) {
        String bestEmail = "";
        int count = 0;
        for (Map.Entry<String, Integer> entry : emailCounts.entrySet()) {
            if (entry.getValue() > count) {
                bestEmail = entry.getKey();
            }
        }
        return bestEmail;
    }

    private void addEmailToEmailCounts(String email, HashMap<String, Integer> map) {
        map.compute(email, (k, v) -> {
            if (v != null) {
                return v + 1;
            }
            return 1;
        });
    }

    private void incrementEbayListingCount() {
        totalEbayListingCount++;
    }

    private void addPriceToEbayListingPrice(double price) {
        totalEbayListingPrice += price;
    }

    private void incrementAmazonListingCount() {
        totalAmazonListingCount++;
    }

    private void addPriceToAmazonListingPrice(double price) {
        totalAmazonListingPrice += price;
    }

    public void setAverages() {
        averageEbayListingPrice = totalEbayListingCount != 0 ? totalEbayListingPrice / totalEbayListingCount : 0;
        averageAmazonListingPrice = totalAmazonListingCount != 0 ? totalAmazonListingPrice / totalAmazonListingCount : 0;
    }

    public void setBestListerEmail(String email) {
        bestListerEmail = email;
    }

    public int getTotalEbayListingCount() {
        return totalEbayListingCount;
    }

    public double getTotalEbayListingPrice() {
        return totalEbayListingPrice;
    }

    public double getTotalAmazonListingCount() {
        return totalAmazonListingCount;
    }

    public double getAverageEbayListingPrice() {
        return averageEbayListingPrice;
    }

    public double getTotalAmazonListingPrice() {
        return totalAmazonListingPrice;
    }

    public double getAverageAmazonListingPrice() {
        return averageAmazonListingPrice;
    }

    public String getBestListerEmail() {
        return bestListerEmail;
    }

    public void setTotalEbayListingCount(int totalEbayListingCount) {
        this.totalEbayListingCount = totalEbayListingCount;
    }

    public void setTotalEbayListingPrice(double totalEbayListingPrice) {
        this.totalEbayListingPrice = totalEbayListingPrice;
    }

    public void setTotalAmazonListingCount(double totalAmazonListingCount) {
        this.totalAmazonListingCount = totalAmazonListingCount;
    }

    public void setAverageEbayListingPrice(double averageEbayListingPrice) {
        this.averageEbayListingPrice = averageEbayListingPrice;
    }

    public void setTotalAmazonListingPrice(double totalAmazonListingPrice) {
        this.totalAmazonListingPrice = totalAmazonListingPrice;
    }

    public void setAverageAmazonListingPrice(double averageAmazonListingPrice) {
        this.averageAmazonListingPrice = averageAmazonListingPrice;
    }
}

class MonthlyReport extends MarketplaceData {

    @SerializedName("Month")
    private String month;

    public MonthlyReport(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
