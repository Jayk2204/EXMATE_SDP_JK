package com.example.exmate_sdp.models;

public class User {
    public String name;
    public String email;

    public User() {
        // empty constructor required for Firebase
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
