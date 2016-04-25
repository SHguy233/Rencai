package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/10.
 */
public class JobIntent {
    private String username;
    private String workPlace;
    private String tradeCategory;
    private String positionCategory;
    private int salary;

    public String getUsername() {
        return username;
    }

    public int getSalary() {
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

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
