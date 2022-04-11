package com.adbu.newsaholic.model;

public class User {

    private String name;
    private String email;
    private String country;
    private String category;
    private boolean isAdmin;

    public User() {
        isAdmin = false;
    }

    public User(String name, String email, String country, String category, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.category = category;
        this.isAdmin = isAdmin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
