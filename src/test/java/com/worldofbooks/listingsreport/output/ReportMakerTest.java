package com.worldofbooks.listingsreport.output;


import com.worldofbooks.listingsreport.api.ApiHandler;
import com.worldofbooks.listingsreport.api.Listing;
import com.worldofbooks.listingsreport.api.ListingDataSet;
import com.worldofbooks.listingsreport.api.Status;
import com.worldofbooks.listingsreport.database.DatabaseHandler;
import com.worldofbooks.listingsreport.database.ListingRepository;
import com.worldofbooks.listingsreport.database.ReferenceDataSet;
import com.worldofbooks.listingsreport.database.validation.ListingValidationResult;
import com.worldofbooks.listingsreport.database.validation.ListingValidator;
import com.worldofbooks.listingsreport.database.validation.ViolationDataSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReportMakerTest {

    private ReportMaker reportMaker;

    @Autowired
    private DatabaseHandler databaseHandler;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private ApiHandler apiHandler;
    @Autowired
    private ListingValidator listingValidator;
    @Autowired
    private ReportProcessor reportProcessor;
    @Autowired
    private ViolationWriterCsv violationWriterCsv;
    @Autowired
    private FileHandlerJsonImpl fileHandlerJson;
    @Autowired
    private FtpClient ftpClient;

    @Captor
    private ArgumentCaptor<ArrayList<ViolationDataSet>> violationDataSetsCaptor;
    @Captor
    private ArgumentCaptor<ArrayList<Listing>> listingsCaptor;

    @Before
    public void setup() {
        reportMaker = new ReportMaker(databaseHandler, listingRepository, apiHandler, listingValidator, reportProcessor);
    }

    @Test
    public void generateListingReport() throws IOException {
        ReflectionTestUtils.setField(ReportMaker.class, "ftpPort", "324");

        ListingDataSet listingDataSet = new ListingDataSet();
        ReferenceDataSet referenceDataSet = new ReferenceDataSet();
        Status status = new Status();
        status.setStatusName("saveReferenceStatusArg");
        referenceDataSet.setStatuses(Collections.singletonList(status));
        listingDataSet.setReferenceDataSet(referenceDataSet);
        when(apiHandler.getListingDataSetFromApi()).thenReturn(listingDataSet);
//       databaseHandler.saveReferences(listingDataSet.getReferenceDataSet());

        ListingValidationResult listingValidationResult = new ListingValidationResult();
        Listing listingResult = new Listing();
        listingResult.setDescription("collectDataListingArg");
        listingValidationResult.setValidatedListings(Collections.singletonList(listingResult));
        listingValidationResult.setViolationDataSets(Collections.singletonList(new ViolationDataSet()));
        Listing listingForViolationDataSet = new Listing();
        listingForViolationDataSet.setDescription("listingForDataSetDescription");
        listingValidationResult.getViolationDataSets().get(0).setListing(listingForViolationDataSet);
        when(listingValidator.validateListings(any(ListingDataSet.class))).thenReturn(listingValidationResult);
//        databaseHandler.saveEntities(validatedListings, listingRepository);

//        violationWriterCsv.processViolations(violationDataSets);
        ReportDto reportDto = new ReportDto();
        reportDto.setBestListerEmail("fileHandlerReportDtoArg");
        when(reportProcessor.collectReportData(any())).thenReturn(reportDto);
//        fileHandlerJson.handleReportData(reportDto);
//        ftpClient.sendToFtp(localReportPath, ftpPath);

        Path importLogPath = Paths.get("importLogPath");
        Path localReportPath = Paths.get("localReportPath");
        String ftpPath = "ftpPath";
        reportMaker.generateListingReport(importLogPath, localReportPath, ftpPath);

        verify(apiHandler, times(1))
            .getListingDataSetFromApi();
        verifyNoMoreInteractions(apiHandler);

        ArgumentCaptor<ReferenceDataSet> referenceDataSetArgument = ArgumentCaptor.forClass(ReferenceDataSet.class);
        verify(databaseHandler, times(1))
            .saveReferences(referenceDataSetArgument.capture());
        ReferenceDataSet referenceArgumentValue = referenceDataSetArgument.getValue();
        List<Status> referenceStatuses = referenceArgumentValue.getStatuses();
        assertThat(referenceStatuses.size(), is(1));
        assertThat(referenceStatuses.get(0).getStatusName(), is("saveReferenceStatusArg"));
        verifyNoMoreInteractions(databaseHandler);

        ArgumentCaptor<ListingDataSet> listingDataSetArgument = ArgumentCaptor.forClass(ListingDataSet.class);
        verify(listingValidator, times(1))
            .validateListings(listingDataSetArgument.capture());
        ListingDataSet listingDataSetArgumentValue = listingDataSetArgument.getValue();
        assertThat(listingDataSetArgumentValue, is(listingDataSet));
        verifyNoMoreInteractions(listingValidator);

        verify(violationWriterCsv, times(1))
            .processViolations(violationDataSetsCaptor.capture());
        List<ViolationDataSet> violationDataSetsArgumentValue = violationDataSetsCaptor.getValue();
        List<ViolationDataSet> violationDataSets = listingValidationResult.getViolationDataSets();
        assertThat(violationDataSetsArgumentValue, is(violationDataSets));
        verifyNoMoreInteractions(violationWriterCsv);

        verify(reportProcessor, times(1))
            .collectReportData(listingsCaptor.capture());
        List<Listing> listingsArgumentValue = listingsCaptor.getValue();
        assertThat(listingsArgumentValue.size(), is(1));
        assertThat(listingsArgumentValue.get(0).getDescription(), is("collectDataListingArg"));
        verifyNoMoreInteractions(reportProcessor);

        ArgumentCaptor<ReportDto> reportDtoArgument = ArgumentCaptor.forClass(ReportDto.class);
        verify(fileHandlerJson, times(1))
            .handleReportData(reportDtoArgument.capture());
        ReportDto reportDtoArgumentValue = reportDtoArgument.getValue();
        assertThat(reportDtoArgumentValue.getBestListerEmail(), is("fileHandlerReportDtoArg"));
        verifyNoMoreInteractions(fileHandlerJson);

        ArgumentCaptor<Path> pathArgument = ArgumentCaptor.forClass(Path.class);
        ArgumentCaptor<String> stringArgument = ArgumentCaptor.forClass(String.class);
        verify(ftpClient, times(1))
            .sendToFtp(pathArgument.capture(), stringArgument.capture());
        Path pathArgumentValue = pathArgument.getValue();
        assertThat(pathArgumentValue, is(localReportPath));
        String stringArgumentValue = stringArgument.getValue();
        assertThat(stringArgumentValue, is(ftpPath));
        verifyNoMoreInteractions(ftpClient);
    }

    @Configuration
    public static class Config {

        @Bean(name = "TestDatabaseHandlerConfiguration")
        @Primary
        public DatabaseHandler databaseHandler() {
            return Mockito.mock(DatabaseHandler.class);
        }

        @Bean(name = "TestListingRepositoryConfiguration")
        @Primary
        public ListingRepository listingRepository() {
            return Mockito.mock(ListingRepository.class);
        }

        @Bean(name = "TestApiHandlerConfiguration")
        @Primary
        public ApiHandler apiHandler() {
            return Mockito.mock(ApiHandler.class);
        }

        @Bean(name = "TestListingValidatorConfiguration")
        @Primary
        public ListingValidator listingValidator() {
            return Mockito.mock(ListingValidator.class);
        }

        @Bean(name = "TestReportProcessorConfiguration")
        @Primary
        public ReportProcessor reportProcessor() {
            return Mockito.mock(ReportProcessor.class);
        }

        @Bean(name = "TestViolationWriterCsvConfiguration")
        @Primary
        public ViolationWriterCsv violationWriterCsv() {
            return Mockito.mock(ViolationWriterCsv.class);
        }

        @Bean(name = "TestFileHandlerJsonImplConfiguration")
        @Primary
        public FileHandlerJsonImpl fileHandlerJson() {
            return Mockito.mock(FileHandlerJsonImpl.class);
        }
        @Bean(name = "TestFtpClientConfiguration")
        @Primary
        public FtpClient ftpClient() {
            return Mockito.mock(FtpClient.class);
        }

    }


}
