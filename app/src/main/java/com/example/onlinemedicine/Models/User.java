package com.example.onlinemedicine.Models;

public class User {

    private int id;
    private String username, email, Phone;

    public User(int id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return Phone;
    }
}
