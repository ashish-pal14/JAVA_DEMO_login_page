package com.example.loginapp.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.loginapp.model.CartLine;
import com.example.loginapp.model.CheckoutForm;
import com.example.loginapp.model.OrderSummary;
import com.example.loginapp.model.Product;
import com.example.loginapp.model.SupportForm;
import com.example.loginapp.service.ShopService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class ShopControllerTest {

    private final ShopService shopService = mock(ShopService.class);
    private final ShopController shopController = new ShopController(shopService);

    @Test
    void showDashboardAddsShopMetrics() {
        when(shopService.getFeaturedProducts()).thenReturn(List.of());
        when(shopService.getCategories()).thenReturn(List.of("Tools", "Materials"));
        when(shopService.getCartItemCount()).thenReturn(4);
        when(shopService.getCartTotal()).thenReturn("$123");
        Model model = new ExtendedModelMap();

        String viewName = shopController.showDashboard(model);

        assertThat(viewName).isEqualTo("shop");
        assertThat(model.asMap()).containsEntry("categoryCount", 2);
        assertThat(model.asMap()).containsEntry("cartItemCount", 4);
        assertThat(model.asMap()).containsEntry("cartTotal", "$123");
    }

    @Test
    void showCatalogUsesRequestedCategory() {
        when(shopService.getCategories()).thenReturn(List.of("Materials", "Tools"));
        when(shopService.getProductsByCategory("Tools")).thenReturn(List.of(new Product(1, "Tool", "Tools", "Desc", "$10", "New")));
        Model model = new ExtendedModelMap();

        String viewName = shopController.showCatalog("Tools", model);

        assertThat(viewName).isEqualTo("catalog");
        assertThat(model.asMap()).containsEntry("selectedCategory", "Tools");
        assertThat(model.asMap()).containsKey("products");
    }

    @Test
    void showProductReturnsDetailView() {
        Product product = new Product(7, "Clamp", "Tools", "Heavy duty clamp", "$12", "Popular");
        when(shopService.findProduct(7)).thenReturn(java.util.Optional.of(product));
        when(shopService.getFeaturedProducts()).thenReturn(List.of(product));
        Model model = new ExtendedModelMap();

        String viewName = shopController.showProduct(7, model);

        assertThat(viewName).isEqualTo("product");
        assertThat(model.asMap()).containsEntry("product", product);
    }

    @Test
    void processCheckoutReturnsConfirmationWhenValid() {
        CheckoutForm checkoutForm = new CheckoutForm();
        checkoutForm.setFullName("Alex Maker");
        checkoutForm.setEmail("alex@example.com");
        checkoutForm.setShippingAddress("12 Workshop Road");
        checkoutForm.setPaymentMethod("card");
        BindingResult bindingResult = new BeanPropertyBindingResult(checkoutForm, "checkoutForm");
        Model model = new ExtendedModelMap();
        when(shopService.getCartTotal()).thenReturn("$120");

        String viewName = shopController.processCheckout(checkoutForm, bindingResult, model);

        assertThat(viewName).isEqualTo("order-confirmation");
        assertThat(model.asMap()).containsEntry("customerName", "Alex Maker");
        assertThat(model.asMap()).containsEntry("cartTotal", "$120");
    }

    @Test
    void processSupportRedirectsWithSuccessMessage() {
        SupportForm supportForm = new SupportForm();
        supportForm.setName("Alex");
        supportForm.setEmail("alex@example.com");
        supportForm.setTopic("Order status");
        supportForm.setMessage("Where is my order?");
        BindingResult bindingResult = new BeanPropertyBindingResult(supportForm, "supportForm");
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String viewName = shopController.processSupport(supportForm, bindingResult, redirectAttributes);

        assertThat(viewName).isEqualTo("redirect:/support");
        assertThat(redirectAttributes.getFlashAttributes().get("supportMessage")).isEqualTo("Thanks, we will reply to alex@example.com shortly.");
    }
}
