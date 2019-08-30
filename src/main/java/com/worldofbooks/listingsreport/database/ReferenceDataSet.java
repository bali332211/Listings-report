package com.worldofbooks.listingsreport.database;

import com.worldofbooks.listingsreport.api.Location;
import com.worldofbooks.listingsreport.api.Marketplace;
import com.worldofbooks.listingsreport.api.Status;

import java.util.List;

public class ReferenceDataSet {
    private List<Status> statuses;
    private List<Location> locations;
    private List<Marketplace> marketplaces;

    public ReferenceDataSet(List<Status> statuses, List<Location> locations, List<Marketplace> marketplaces) {
        this.statuses = statuses;
        this.locations = locations;
        this.marketplaces = marketplaces;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Marketplace> getMarketplaces() {
        return marketplaces;
    }
}