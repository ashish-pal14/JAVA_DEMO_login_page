package com.example.loginapp.model;

public class CartLine {

    private final Product product;
    private final int quantity;

    public CartLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLineTotal() {
        int price = Integer.parseInt(product.getPrice().replace("$", ""));
        return "$" + (price * quantity);
    }
}
