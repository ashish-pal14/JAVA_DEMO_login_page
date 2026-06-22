package com.example.loginapp.model;

public class Product {

    private final long id;
    private final String name;
    private final String category;
    private final String description;
    private final String price;
    private final String status;

    public Product(long id, String name, String category, String description, String price, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
