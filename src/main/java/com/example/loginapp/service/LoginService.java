package com.example.loginapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final String validUsername;
    private final String validPassword;

    public LoginService(
            @Value("${app.auth.username:admin}") String validUsername,
            @Value("${app.auth.password:admin123}") String validPassword) {
        this.validUsername = validUsername;
        this.validPassword = validPassword;
    }

    public boolean authenticate(String username, String password) {
        return validUsername.equals(username) && validPassword.equals(password);
    }
}