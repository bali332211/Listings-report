package com.worldofbooks.listingsreport;

import com.worldofbooks.listingsreport.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportUtil {

    private DatabaseService databaseService;

    @Autowired
    public ReportUtil(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }



}
