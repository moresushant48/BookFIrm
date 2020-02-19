package com.example.bookfirm.models;

public class User {

    private int id;

    private String username;

    private String email;

    private String address;

    private String mobileno;

    private String password;

    public User() {

    }

    public User(String username, String email, String address, String mobileno, String password) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.mobileno = mobileno;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
