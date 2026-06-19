package com.example.loginapp.web;

import com.example.loginapp.model.LoginForm;
import com.example.loginapp.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping({"/", "/login"})
    public String showLoginPage(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
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
        return "welcome";
    }
}