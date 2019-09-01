package com.worldofbooks.listingsreport.output;

import com.worldofbooks.listingsreport.ListingBuilder;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.database.MarketplaceRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReportDataCollectorTest {

    private ReportDataCollector reportDataCollector;

    @Autowired
    private FileHandlerJson fileHandlerJson;
    @Autowired
    private MarketplaceRepository marketplaceRepository;

    @Before
    public void setup() {
        reportDataCollector = new ReportDataCollector(marketplaceRepository, fileHandlerJson);
    }

    @Test
    public void collectReportData() {
        Marketplace marketplace = new Marketplace();
        marketplace.setId(1);
        when(marketplaceRepository.findByMarketplaceName(isNull())).thenReturn(marketplace);

        Listing listingAllowed = new ListingBuilder("6022bade-659e-448a-a9fc-f588609f9b6b", "testTitle")
                .listingPrice(19.12)
                .listingStatus(4)
                .locationId("testLocationId")
                .marketplace(1)
                .currency("USD")
                .description("testDescription")
                .uploadTime(LocalDate.of(2018, 10, 2))
                .ownerEmailAddress("testEmail@email.com")
                .quantity(1)
                .createListing();

        Listing listingAllowed2 = new ListingBuilder("a9fa91fe-4710-4991-9b66-547d522ef9f6", "testTitle")
                .listingPrice(20.76)
                .listingStatus(4)
                .locationId("testLocationId")
                .marketplace(1)
                .currency("USD")
                .description("testDescription")
                .uploadTime(LocalDate.of(2018, 10, 3))
                .ownerEmailAddress("testEmail@email.com")
                .quantity(1)
                .createListing();

        Listing listingAllowed3 = new ListingBuilder("a9fa91fe-4710-4991-9b66-547d522ef9f6", "testTitle")
                .listingPrice(50.12)
                .listingStatus(4)
                .locationId("testLocationId")
                .marketplace(1)
                .currency("USD")
                .description("testDescription")
                .uploadTime(LocalDate.of(2018, 11, 2))
                .ownerEmailAddress("another@email.com")
                .quantity(1)
                .createListing();

        reportDataCollector.collectReportData(Arrays.asList(listingAllowed, listingAllowed2, listingAllowed3));

        ArgumentCaptor<ReportDto> reportDtoArgument = ArgumentCaptor.forClass(ReportDto.class);
        verify(fileHandlerJson, times(1))
                .handleReportData(reportDtoArgument.capture());

        ReportDto reportDtoArgumentValue = reportDtoArgument.getValue();
        assertThat(reportDtoArgumentValue.getListingCount(), Matchers.is(3));
        assertThat(reportDtoArgumentValue.getAverageEbayListingPrice(), Matchers.is(30D));
        assertThat(reportDtoArgumentValue.getAverageAmazonListingPrice(), Matchers.is(0D));
        assertThat(reportDtoArgumentValue.getBestListerEmail(), Matchers.is("testEmail@email.com"));

        List<MonthlyReport> monthlyReports = reportDtoArgumentValue.getMonthlyReports();
        int monthlyReportsSize = monthlyReports.size();
        assertThat(monthlyReportsSize, Matchers.is(2));
        MonthlyReport monthlyReportFirst = monthlyReports.get(0);
        assertThat(monthlyReportFirst.getTotalEbayListingCount(), Matchers.is(2));
        MonthlyReport monthlyReportLast = monthlyReports.get(monthlyReportsSize - 1);
        assertThat(monthlyReportLast.getBestListerEmail(), Matchers.is("another@email.com"));
        assertThat(monthlyReportLast.getTotalEbayListingCount(), Matchers.is(1));
        assertThat(monthlyReportLast.getTotalEbayListingPrice(), Matchers.is(50.12D));
        assertThat(monthlyReportLast.getAverageEbayListingPrice(), Matchers.is(50.12D));
        verifyNoMoreInteractions(fileHandlerJson);

    }


    @Configuration
    public static class Config {

        @Bean(name = "TestFileHandlerJsonConfiguration")
        @Primary
        public FileHandlerJson fileHandlerJson() {
            return Mockito.mock(FileHandlerJson.class);
        }

        @Bean(name = "TestMarketplaceRepositoryConfiguration")
        @Primary
        public MarketplaceRepository marketplaceRepository() {
            return Mockito.mock(MarketplaceRepository.class);
        }

    }

}
