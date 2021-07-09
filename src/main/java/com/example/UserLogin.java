package com.example;

public class UserLogin {
    private String username;
    private String password;
    private int id;
    private String access;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setAccess(String access) {
        this.access = access;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public int getID() {
        return this.id;
    }
    public String getAccess() {
        return this.access;
    }
}