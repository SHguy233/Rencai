package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/10.
 */
public class JobIntent {
    private String username;
    private String workPlace;
    private String tradeCategory;
    private String positionCategory;
    private String salary;

    public String getUsername() {
        return username;
    }

    public String getSalary() {
        return salary;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public String getTradeCategory() {
        return tradeCategory;
    }

    public String getPositionCategory() {
        return positionCategory;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setTradeCategory(String tradeCategory) {
        this.tradeCategory = tradeCategory;
    }

    public void setPositionCategory(String positionCategory) {
        this.positionCategory = positionCategory;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
