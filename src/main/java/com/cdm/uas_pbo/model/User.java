package com.cdm.uas_pbo.model;

public class User { // Intinya data yang disimpan
    private final int id;
    private final String email;

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}