package com.example.loginapp.model;

public class OrderSummary {

    private final String orderNumber;
    private final String status;
    private final String total;

    public OrderSummary(String orderNumber, String status, String total) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.total = total;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal() {
        return total;
    }
}
