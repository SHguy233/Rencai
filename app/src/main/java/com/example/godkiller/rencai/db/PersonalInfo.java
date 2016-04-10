package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/9.
 */
public class PersonalInfo {
    private String username;
    private String name;
    private String gender;
    private String birth;
    private String workExpTime;
    private String acceptRemote;
    private String phone;


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    public String getWorkExpTime() {
        return workExpTime;
    }

    public String getAcceptRemote() {
        return acceptRemote;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setWorkExpTime(String workExpTime) {
        this.workExpTime = workExpTime;
    }

    public void setAcceptRemote(String acceptRemote) {
        this.acceptRemote = acceptRemote;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
