package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/16.
 */
public class CompanyInfo {
    private String username;
    private String company;
    private String trade;
    private String nature;
    private String scale;
    private String address;
    private String business;

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getCompany() {
        return company;
    }

    public String getTrade() {
        return trade;
    }

    public String getNature() {
        return nature;
    }

    public String getScale() {
        return scale;
    }

    public String getBusiness() {
        return business;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBusiness(String business) {
        this.business = business;
    }
}
