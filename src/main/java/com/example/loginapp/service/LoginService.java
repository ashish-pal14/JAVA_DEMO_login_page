package com.example.loginapp.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final Map<String, String> credentials = new ConcurrentHashMap<>();

    public LoginService(
            @Value("${app.auth.username:admin}") String validUsername,
            @Value("${app.auth.password:admin123}") String validPassword) {
        resetCredentials();
        credentials.put(validUsername, validPassword);
    }

    public boolean authenticate(String username, String password) {
        return password != null && password.equals(credentials.get(username));
    }

    public void register(String username, String password) {
        credentials.put(username, password);
    }

    public void resetCredentials() {
        credentials.clear();
        credentials.put("admin", "admin123");
    }
}