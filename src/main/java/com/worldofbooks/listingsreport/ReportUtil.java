package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportUtil {

    private MarketplaceRepository marketplaceRepository;

    private final ReportDto reportDto;
    private final MarketplaceData marketplaceData;
    private final int ebayId;
    private final int amazonId;

    @Autowired
    public ReportUtil(MarketplaceRepository marketplaceRepository) {
        this.marketplaceRepository = marketplaceRepository;

        Marketplace ebay = marketplaceRepository.findByMarketplaceName("Ebay");
        this.ebayId = ebay.getId();
        Marketplace amazon = marketplaceRepository.findByMarketplaceName("Amazon");
        this.amazonId = amazon.getId();
        this.reportDto = new ReportDto();
        this.marketplaceData = new MarketplaceData(ebayId, amazonId);
    }

    public void collectReportData(Listing listing) {
        MonthlyReport[] monthlyReports = new MonthlyReport[12];

        reportDto.incrementListingCount();

        marketplaceData.updateMarketPlaceDataWithListing(listing);

    }

    private static final class ReportDto {
        private int listingCount = 0;
        private int totalEbayListingCount = 0;
        private int totalAmazonListingCount = 0;
        private long totalEbayListingPrice = 0;
        private long totalAmazonListingPrice = 0;
        private long averageEbayListingPrice = 0;
        private long averageAmazonListingPrice = 0;

        private void incrementListingCount() {
            listingCount++;
        }

        public int getListingCount() {
            return listingCount;
        }

        public int getTotalEbayListingCount() {
            return totalEbayListingCount;
        }

        public int getTotalAmazonListingCount() {
            return totalAmazonListingCount;
        }

        public long getTotalEbayListingPrice() {
            return totalEbayListingPrice;
        }

        public long getTotalAmazonListingPrice() {
            return totalAmazonListingPrice;
        }

        public long getAverageEbayListingPrice() {
            return averageEbayListingPrice;
        }

        public long getAverageAmazonListingPrice() {
            return averageAmazonListingPrice;
        }
    }

    private static final class MarketplaceData {
        private int totalEbayListingCount = 0;
        private int totalAmazonListingCount = 0;
        private long totalEbayListingPrice = 0;
        private long totalAmazonListingPrice = 0;
        private long averageEbayListingPrice = 0;
        private long averageAmazonListingPrice = 0;

        private final int ebayId;
        private final int amazonId;

        public MarketplaceData(int ebayId, int amazonId) {
            this.ebayId = ebayId;
            this.amazonId = amazonId;
        }

        private void updateMarketPlaceDataWithListing(Listing listing) {
            int marketplaceId = listing.getMarketplace();
            long listingPrice = listing.getListingPrice();

            incrementMarketplaceListingCount(marketplaceId);
            addPriceToMarketplaceListingPrice(listingPrice, marketplaceId);
        }

        private void incrementMarketplaceListingCount(int id) {
            if (id == ebayId) {
                incrementEbayListingCount();
            } else if (id == amazonId) {
                incrementAmazonListingCount();
            }
        }

        private void addPriceToMarketplaceListingPrice(long price, int id) {
            if (id == ebayId) {
                addPriceToEbayListingPrice(price);
            } else if (id == amazonId) {
                addPriceToAmazonListingPrice(price);
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

    private static final class MonthlyReport {
        private final int month;

        public MonthlyReport(int month) {
            this.month = month;
        }
    }
}
