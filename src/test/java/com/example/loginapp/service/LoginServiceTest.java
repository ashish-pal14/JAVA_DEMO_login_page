package com.example.loginapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LoginServiceTest {

    @Test
    void authenticateReturnsTrueForConfiguredCredentials() {
        LoginService loginService = new LoginService("admin", "admin123");

        assertThat(loginService.authenticate("admin", "admin123")).isTrue();
    }

    @Test
    void authenticateReturnsFalseForInvalidUsername() {
        LoginService loginService = new LoginService("admin", "admin123");

        assertThat(loginService.authenticate("user", "admin123")).isFalse();
    }

    @Test
    void authenticateReturnsFalseForInvalidPassword() {
        LoginService loginService = new LoginService("admin", "admin123");

        assertThat(loginService.authenticate("admin", "wrong")).isFalse();
    }
}