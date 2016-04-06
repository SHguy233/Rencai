package com.example.godkiller.rencai.db;

/**
 * Created by GodKiller on 2016/4/5.
 */
public class User {

    private String username;
    private int id;
    private String password;
    private String identity;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentity() {
        return identity;
    }

}
