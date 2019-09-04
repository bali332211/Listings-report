package com.worldofbooks.listingsreport.output;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class FileHandlerJsonImplTest {

    @ClassRule
    public static final TemporaryFolder TEMPORARY_FOLDER = new TemporaryFolder();

    @Test
    public void handleReportData() throws IOException {
        ReportDto reportDto = new ReportDto();
        reportDto.setListingCount(4);
        reportDto.setBestListerEmail("email");
        reportDto.setMonthlyReports(Collections.singletonList(new MonthlyReport("month")));

        Path localReportPath = TEMPORARY_FOLDER.newFile("testReport.json").toPath();
        FileHandlerJson fileHandlerJson = new FileHandlerJsonImpl(localReportPath);
        fileHandlerJson.handleReportData(reportDto);

        try (BufferedReader reader = new BufferedReader(new FileReader(localReportPath.toString()))) {
            List<String> lines = new ArrayList<>();
            String strCurrentLine;
            while ((strCurrentLine = reader.readLine()) != null) {
                lines.add(strCurrentLine);
            }
            assertThat(lines.size(), is(1));
        }
    }

}
