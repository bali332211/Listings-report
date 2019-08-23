package com.worldofbooks.listingsreport.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "marketplaces")
public class Marketplace {

    @Id
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
