package com.example.cpu11398_local.etalk.presentation.model;

public class User {

    private String name;
    private String username;
    private String password;
    private String phone;
    private String status;

    public User() {
    }

    public User(String name, String username, String password, String phone, String status) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
