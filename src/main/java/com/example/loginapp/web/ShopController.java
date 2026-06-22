package com.example.loginapp.web;

import com.example.loginapp.model.CheckoutForm;
import com.example.loginapp.model.SupportForm;
import com.example.loginapp.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping({"/shop", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("featuredProducts", shopService.getFeaturedProducts());
        model.addAttribute("categoryCount", shopService.getCategories().size());
        model.addAttribute("cartItemCount", shopService.getCartItemCount());
        model.addAttribute("cartTotal", shopService.getCartTotal());
        return "shop";
    }

    @GetMapping("/catalog")
    public String showCatalog(@RequestParam(value = "category", required = false) String category, Model model) {
        model.addAttribute("categories", shopService.getCategories());
        model.addAttribute("selectedCategory", category == null ? "all" : category);
        model.addAttribute("products", shopService.getProductsByCategory(category));
        return "catalog";
    }

    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable long id, Model model) {
        model.addAttribute("product", shopService.findProduct(id).orElseThrow());
        model.addAttribute("relatedProducts", shopService.getFeaturedProducts());
        return "product";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("cartLines", shopService.getCartLines());
        model.addAttribute("cartTotal", shopService.getCartTotal());
        return "cart";
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model) {
        if (!model.containsAttribute("checkoutForm")) {
            model.addAttribute("checkoutForm", new CheckoutForm());
        }
        model.addAttribute("cartTotal", shopService.getCartTotal());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@Valid @ModelAttribute CheckoutForm checkoutForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cartTotal", shopService.getCartTotal());
            return "checkout";
        }

        model.addAttribute("orderNumber", "WS-" + System.currentTimeMillis());
        model.addAttribute("cartTotal", shopService.getCartTotal());
        model.addAttribute("customerName", checkoutForm.getFullName());
        return "order-confirmation";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("orders", shopService.getOrderHistory());
        return "orders";
    }

    @GetMapping("/account")
    public String showAccount(Model model) {
        model.addAttribute("accountName", "Workshop Maker");
        model.addAttribute("membershipLevel", "Gold Builder");
        model.addAttribute("loyaltyPoints", 1480);
        return "account";
    }

    @GetMapping("/support")
    public String showSupport(Model model) {
        if (!model.containsAttribute("supportForm")) {
            model.addAttribute("supportForm", new SupportForm());
        }
        return "support";
    }

    @PostMapping("/support")
    public String processSupport(@Valid @ModelAttribute SupportForm supportForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supportForm", bindingResult);
            redirectAttributes.addFlashAttribute("supportForm", supportForm);
            return "redirect:/support";
        }

        redirectAttributes.addFlashAttribute("supportMessage", "Thanks, we will reply to " + supportForm.getEmail() + " shortly.");
        return "redirect:/support";
    }
}
