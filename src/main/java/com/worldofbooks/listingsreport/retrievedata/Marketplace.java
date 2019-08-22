package com.worldofbooks.listingsreport.retrievedata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Marketplace {

    private int id;
    @JsonProperty("marketplace_name")
    private String marketplaceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }
}
