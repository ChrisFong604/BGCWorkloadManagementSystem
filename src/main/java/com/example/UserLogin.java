package com.example;

public class UserLogin {
    private String username;
    private String password;
    private int id;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setID(int id) {
        this.id = id;
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
}