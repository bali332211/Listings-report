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

    @Value(value = "${worldofbooks.ebay.name}")
    private String ebayName;
    @Value(value = "${worldofbooks.amazon.name}")
    private String amazonName;

    @Autowired
    public ReportDataCollector(MarketplaceRepository marketplaceRepository) {
        this.marketplaceRepository = marketplaceRepository;
    }

    @Override
    public ReportDto collectReportData(List<Listing> listings) {
        Marketplace ebay = marketplaceRepository.findByMarketplaceName(ebayName);
        Marketplace amazon = marketplaceRepository.findByMarketplaceName(amazonName);
        int ebayId = ebay.getId();
        int amazonId = amazon.getId();

        return makeReportDto(listings, ebayId, amazonId);
    }

    private ReportDto makeReportDto(List<Listing> listings, int ebayId, int amazonId) {
        int listingCount = listings.size();

        ReportDto reportDto = new ReportDto();
        reportDto.setListingCount(listingCount);
        reportDto.updateMarketPlaceDataWithListing(listings, ebayId, amazonId);

        List<MonthlyReport> monthlyReports = getMonthlyReports(listings, ebayId, amazonId);
        reportDto.setMonthlyReports(monthlyReports);

        return reportDto;
    }

    private List<MonthlyReport> getMonthlyReports(List<Listing> listings, int ebayId, int amazonId) {
        List<Listing> listingsWithUploadTime = getListingsWithUploadTime(listings);

        List<MonthlyReport> monthlyReports = new ArrayList<>();
        List<Listing> listingsOfCurrentMonth = new ArrayList<>();

        int previousYear = -1;
        int previousMonth = -1;

        for (Listing listing : listingsWithUploadTime) {
            LocalDate uploadTime = listing.getUploadTime();
            int currentYear = uploadTime.getYear();
            int currentMonth = uploadTime.getMonthValue();

            if (previousYear == -1) {
                previousYear = currentYear;
                previousMonth = currentMonth;
            }

            if (previousYear != currentYear || previousMonth != currentMonth) {
                updateMonthlyReportsWithListingsOfCurrentMonth(monthlyReports, listingsOfCurrentMonth, previousYear + "/" + previousMonth, ebayId, amazonId);
                listingsOfCurrentMonth.clear();
            }
            listingsOfCurrentMonth.add(listing);
            previousYear = currentYear;
            previousMonth = currentMonth;
        }
        updateMonthlyReportsWithListingsOfCurrentMonth(monthlyReports, listingsOfCurrentMonth, previousYear + "/" + previousMonth, ebayId, amazonId);
        return monthlyReports;
    }

    private void updateMonthlyReportsWithListingsOfCurrentMonth(List<MonthlyReport> monthlyReports,
                                                                List<Listing> listingsOfCurrentMonth,
                                                                String monthName,
                                                                int ebayId,
                                                                int amazonId) {
        MonthlyReport monthlyReport = new MonthlyReport(monthName);
        monthlyReport.updateMarketPlaceDataWithListing(listingsOfCurrentMonth, ebayId, amazonId);
        monthlyReports.add(monthlyReport);
    }

    private List<Listing> getListingsWithUploadTime(List<Listing> listings) {
        return listings.stream()
            .filter(listing -> listing.getUploadTime() != null)
            .sorted(new SortByUploadTime())
            .collect(Collectors.toList());
    }

    public static final class SortByUploadTime implements Comparator<Listing> {
        @Override
        public int compare(Listing o1, Listing o2) {
            LocalDate uploadTime = o1.getUploadTime();
            LocalDate uploadTime2 = o2.getUploadTime();

            if (uploadTime == null || uploadTime2 == null) {

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
