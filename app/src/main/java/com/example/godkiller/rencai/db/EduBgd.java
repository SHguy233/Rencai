package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class EduBgd {
    private String username;
    private String enrollDate;
    private String graduateDate;
    private String degree;
    private String college;
    private String major;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public String getGraduateDate() {
        return graduateDate;
    }

    public String getDegree() {
        return degree;
    }

    public String getCollege() {
        return college;
    }

    public String getMajor() {
        return major;
    }

    public void setGraduateDate(String graduateDate) {
        this.graduateDate = graduateDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setMajor(String major) {
        this.major = major;
    }

}
