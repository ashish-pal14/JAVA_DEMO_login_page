package com.example.loginapp.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.loginapp.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LoginService loginService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        loginService.resetCredentials();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void landingPageIsAvailable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void signupFlowRedirectsToLogin() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("username", "parallel-maker")
                        .param("password", "build123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginWithDefaultsReachesShopView() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "admin123"))
                .andExpect(status().isOk())
                .andExpect(view().name("shop"))
                .andExpect(model().attributeExists("featuredProducts", "categoryCount", "cartItemCount", "cartTotal"));
    }

    @Test
    void signupCreatesASecondValidLogin() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("username", "parallel-maker")
                        .param("password", "build123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(post("/login")
                        .param("username", "parallel-maker")
                        .param("password", "build123"))
                .andExpect(status().isOk())
                .andExpect(view().name("shop"));
    }
}
