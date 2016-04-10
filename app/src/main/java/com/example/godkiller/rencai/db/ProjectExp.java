package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/7.
 */
public class ProjectExp {
    private String username;
    private String startDate;
    private String finishDate;
    private String prjectDesc;
    private String project;

    public void setPrjectDesc(String prjectDesc) {
        this.prjectDesc = prjectDesc;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPrjectDesc() {
        return prjectDesc;
    }

    public String getProject() {
        return project;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }


    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }



}
