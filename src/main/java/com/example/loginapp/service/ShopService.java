package com.example.loginapp.service;

import com.example.loginapp.model.CartLine;
import com.example.loginapp.model.OrderSummary;
import com.example.loginapp.model.Product;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    private final List<Product> products = List.of(
            new Product(1, "Precision Tool Kit", "Tools", "Complete kit for careful measuring, cutting, and assembly.", "$84", "New arrival"),
            new Product(2, "Workbench Clamp Set", "Tools", "Heavy-duty clamps for secure project setup.", "$46", "Popular"),
            new Product(3, "Storage Wall System", "Storage", "Modular wall rails and bins for a cleaner workspace.", "$112", "Best value"),
            new Product(4, "Oak Project Board Pack", "Materials", "Ready-to-use boards for furniture and decor projects.", "$68", "In stock"),
            new Product(5, "Finish & Care Bundle", "Materials", "Protective oils, cloths, and finishing supplies.", "$39", "Featured"),
            new Product(6, "Safety Starter Kit", "Safety", "Gloves, goggles, masks, and first-aid essentials.", "$29", "Limited"));

    private final List<CartLine> cart = List.of(
            new CartLine(products.get(0), 1),
            new CartLine(products.get(2), 2),
            new CartLine(products.get(4), 1));

    private final List<OrderSummary> orderHistory = List.of(
            new OrderSummary("WS-10021", "Delivered", "$162"),
            new OrderSummary("WS-10022", "Processing", "$112"),
            new OrderSummary("WS-10023", "Packed", "$78"));

    public List<Product> getFeaturedProducts() {
        return products.stream().limit(3).collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<String> getCategories() {
        return products.stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) {
            return getProducts();
        }

        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Optional<Product> findProduct(long id) {
        return products.stream().filter(product -> product.getId() == id).findFirst();
    }

    public List<CartLine> getCartLines() {
        return cart;
    }

    public String getCartTotal() {
        int total = cart.stream()
                .mapToInt(line -> Integer.parseInt(line.getProduct().getPrice().replace("$", "")) * line.getQuantity())
                .sum();
        return "$" + total;
    }

    public int getCartItemCount() {
        return cart.stream().mapToInt(CartLine::getQuantity).sum();
    }

    public List<OrderSummary> getOrderHistory() {
        return orderHistory.stream()
                .sorted(Comparator.comparing(OrderSummary::getOrderNumber).reversed())
                .collect(Collectors.toList());
    }
}
