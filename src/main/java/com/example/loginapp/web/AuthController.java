package com.example.loginapp.web;

import com.example.loginapp.model.LoginForm;
import com.example.loginapp.model.SignupForm;
import com.example.loginapp.service.LoginService;
import com.example.loginapp.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class AuthController {

    private final LoginService loginService;
    private final ShopService shopService;

    public AuthController(LoginService loginService, ShopService shopService) {
        this.loginService = loginService;
        this.shopService = shopService;
    }

    @GetMapping({"/", "/home"})
    public String showLandingPage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        if (model.containsAttribute("signupSuccess")) {
            model.addAttribute("signupSuccess", model.asMap().get("signupSuccess"));
        }
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        if (!model.containsAttribute("signupForm")) {
            model.addAttribute("signupForm", new SignupForm());
        }
        return "signup";
    }

    @PostMapping("/login")
    public String processLogin(@Valid LoginForm loginForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        if (!loginService.authenticate(loginForm.getUsername(), loginForm.getPassword())) {
            model.addAttribute("loginError", "Invalid username or password");
            model.addAttribute("loginForm", loginForm);
            return "login";
        }

        model.addAttribute("username", loginForm.getUsername());
        model.addAttribute("featuredProducts", shopService.getFeaturedProducts());
        model.addAttribute("categoryCount", shopService.getCategories().size());
        model.addAttribute("cartItemCount", shopService.getCartItemCount());
        model.addAttribute("cartTotal", shopService.getCartTotal());
        return "shop";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        loginService.register(signupForm.getUsername(), signupForm.getPassword());
        redirectAttributes.addFlashAttribute("signupSuccess", "Account created. Please sign in.");
        return "redirect:/login";
    }
}