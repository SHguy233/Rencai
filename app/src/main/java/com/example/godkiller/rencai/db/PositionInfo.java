package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/16.
 */
public class PositionInfo {
    private String username;
    private String company;
    private String city;
    private String position;
    private int num;
    private int salary;
    private String degree;
    private String workexp;
    private String condition;

    public String getUsername() {
        return username;
    }

    public String getCompany() {
        return company;
    }

    public String getCity() {
        return city;
    }

    public String getPosition() {
        return position;
    }

    public int getNum() {
        return num;
    }

    public int getSalary() {
        return salary;
    }

    public String getDegree() {
        return degree;
    }

    public String getWorkexp() {
        return workexp;
    }

    public String getCondition() {
        return condition;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setWorkexp(String workexp) {
        this.workexp = workexp;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
