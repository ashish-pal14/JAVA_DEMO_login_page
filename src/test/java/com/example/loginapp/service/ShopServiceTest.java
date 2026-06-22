package com.example.loginapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ShopServiceTest {

    private final ShopService shopService = new ShopService();

    @Test
    void getCategoriesReturnsSortedUniqueCategories() {
        assertThat(shopService.getCategories()).containsExactly("Materials", "Safety", "Storage", "Tools");
    }

    @Test
    void getProductsByCategoryFiltersResults() {
        assertThat(shopService.getProductsByCategory("Tools")).hasSize(2);
    }

    @Test
    void getCartSummaryReflectsCartContent() {
        assertThat(shopService.getCartItemCount()).isEqualTo(4);
        assertThat(shopService.getCartTotal()).isEqualTo("$347");
    }
}
