package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class WorkExp {
    private String username;
    private String entryDate;
    private String leaveDate;
    private String position;
    private String company;
    private String trade;
    private String jobDesc;

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public String getPosition() {
        return position;
    }

    public String getCompany() {
        return company;
    }

    public String getTrade() {
        return trade;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }



}
