package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReportDataCollector implements ReportProcessor {

    private MarketplaceRepository marketplaceRepository;
    private FileHandlerJson fileHandlerJSON;

    @Value(value = "${worldofbooks.ebay.name}")
    private String ebayName;
    @Value(value = "${worldofbooks.amazon.name}")
    private String amazonName;

    @Autowired
    public ReportDataCollector(MarketplaceRepository marketplaceRepository, FileHandlerJson fileHandlerJSON) {
        this.marketplaceRepository = marketplaceRepository;
        this.fileHandlerJSON = fileHandlerJSON;
    }

    @Override
    public void collectReportData(List<Listing> listings) {
        Marketplace ebay = marketplaceRepository.findByMarketplaceName(ebayName);
        Marketplace amazon = marketplaceRepository.findByMarketplaceName(amazonName);
        int ebayId = ebay.getId();
        int amazonId = amazon.getId();

        ReportDto reportDto = makeReportDto(listings, ebayId, amazonId);

        fileHandlerJSON.handleReportData(reportDto);
    }

    private ReportDto makeReportDto(List<Listing> listings, int ebayId, int amazonId) {
        int listingCount = listings.size();

        ReportDto reportDto = new ReportDto();
        reportDto.setListingCount(listingCount);
        reportDto.updateMarketPlaceDataWithListing(listings, ebayId, amazonId);


        List<MonthlyReport> monthsInReport = getMonthlyReports(listings, ebayId, amazonId);
        reportDto.setMonthlyReports(monthsInReport);

        fileHandlerJSON.handleReportData(reportDto);
        return reportDto;
    }

    private List<MonthlyReport> getMonthlyReports(List<Listing> listings, int ebayId, int amazonId) {
        List<Listing> listingsWithUploadTime = listings.stream()
            .filter(listing -> listing.getUploadTime() != null)
            .sorted(new SortByUploadTime())
            .collect(Collectors.toList());

        List<MonthlyReport> monthlyReports = new ArrayList<>();

        List<Listing> listingsOfCurrentMonth = new ArrayList<>();
        int lastYear = -1;
        int lastMonth = -1;

        for (Listing listing : listingsWithUploadTime) {
            LocalDate uploadTime = listing.getUploadTime();
            if (uploadTime != null) {
                int currentYear = uploadTime.getYear();
                int currentMonth = uploadTime.getMonthValue();

                if (lastYear != -1) {
                    if (lastYear != currentYear || lastMonth != currentMonth) {
                        MonthlyReport monthlyReport = new MonthlyReport(lastYear + "/" + lastMonth);
                        monthlyReport.updateMarketPlaceDataWithListing(listingsOfCurrentMonth, ebayId, amazonId);
                        monthlyReports.add(monthlyReport);

                        listingsOfCurrentMonth.clear();
                    }
                }
                listingsOfCurrentMonth.add(listing);
                lastYear = currentYear;
                lastMonth = currentMonth;
            }
        }
        return monthlyReports;
    }

//    public static final class ReportDto extends MarketplaceData {
//        @SerializedName(value = "Total listing count")
//        private int listingCount = 0;
//        @SerializedName(value = "Monthly reports")
//        private List<MonthInReport> monthsInReport = new ArrayList<>();
//
//        public int getListingCount() {
//            return listingCount;
//        }
//
//        public void setListingCount(int listingCount) {
//            this.listingCount = listingCount;
//        }
//
//        public List<MonthInReport> getMonthsInReport() {
//            return monthsInReport;
//        }
//
//        public void setMonthsInReport(List<MonthInReport> monthsInReport) {
//            this.monthsInReport = monthsInReport;
//        }
//    }
//
//    private static class MarketplaceData {
//        @SerializedName(value = "Total eBay listing count")
//        private int totalEbayListingCount = 0;
//        @SerializedName(value = "Total eBay listing price")
//        private long totalEbayListingPrice = 0;
//        @SerializedName(value = "Total Amazon listing count")
//        private int totalAmazonListingCount = 0;
//        @SerializedName(value = "Average eBay listing price")
//        private long averageEbayListingPrice = 0;
//        @SerializedName(value = "Total Amazon listing price")
//        private long totalAmazonListingPrice = 0;
//        @SerializedName(value = "Average Amazon listing price")
//        private long averageAmazonListingPrice = 0;
//        @SerializedName(value = "Best lister email address")
//        private String bestListerEmail = "";
//
//        public void updateMarketPlaceDataWithListing(List<Listing> listings, int ebayId, int amazonId) {
//            for (Listing listing : listings) {
//                int marketplaceId = listing.getMarketplace();
//                long listingPrice = listing.getListingPrice();
//
//                if (marketplaceId == ebayId) {
//                    incrementEbayListingCount();
//                    addPriceToEbayListingPrice(listingPrice);
//                } else if (marketplaceId == amazonId) {
//                    incrementAmazonListingCount();
//                    addPriceToAmazonListingPrice(listingPrice);
//                }
//            }
//
//            String bestLister = getBestListerEmailFromListings(listings);
//            setBestListerEmail(bestLister);
//            setAverages();
//
//        }
//
//        private String getBestListerEmailFromListings(List<Listing> listings) {
//            HashMap<String, Integer> emailCounts = new HashMap<>();
//
//            for (Listing listing : listings) {
//                String email = listing.getOwnerEmailAddress();
//                addEmailToEmailCounts(email, emailCounts);
//            }
//
//            return getBestListerEmailFromMap(emailCounts);
//        }
//
//        private String getBestListerEmailFromMap(HashMap<String, Integer> emailCounts) {
//            String bestEmail = "";
//            int count = 0;
//            for (Map.Entry<String, Integer> entry : emailCounts.entrySet()) {
//                if (entry.getValue() > count) {
//                    bestEmail = entry.getKey();
//                }
//            }
//            return bestEmail;
//        }
//
//        private void addEmailToEmailCounts(String email, HashMap<String, Integer> map) {
//            map.compute(email, (k, v) -> {
//                if (v != null) {
//                    return v + 1;
//                }
//                return 1;
//            });
//        }
//
//        private void incrementEbayListingCount() {
//            totalEbayListingCount++;
//        }
//
//        private void addPriceToEbayListingPrice(long price) {
//            totalEbayListingPrice += price;
//        }
//
//        private void incrementAmazonListingCount() {
//            totalAmazonListingCount++;
//        }
//
//        private void addPriceToAmazonListingPrice(long price) {
//            totalAmazonListingPrice += price;
//        }
//
//        public void setAverages() {
//            averageEbayListingPrice = totalEbayListingCount != 0 ? totalEbayListingPrice / totalEbayListingCount : 0;
//            averageAmazonListingPrice = totalAmazonListingCount != 0 ? totalAmazonListingPrice / totalAmazonListingCount : 0;
//        }
//
//        public void setBestListerEmail(String email) {
//            bestListerEmail = email;
//        }
//    }

    public static final class SortByUploadTime implements Comparator<Listing> {
        @Override
        public int compare(Listing o1, Listing o2) {
            LocalDate uploadTime = o1.getUploadTime();
            LocalDate uploadTime2 = o2.getUploadTime();

            if(uploadTime == null || uploadTime2 == null) {

            }

            if (uploadTime.compareTo(uploadTime2) > 0) {
                return 1;
            } else if (uploadTime.compareTo(uploadTime2) < 0) {
                return -1;
            }
            return 0;
        }
    }
}
