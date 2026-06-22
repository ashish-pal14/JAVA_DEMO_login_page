package com.example.loginapp.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.loginapp.model.LoginForm;
import com.example.loginapp.model.SignupForm;
import com.example.loginapp.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class AuthControllerTest {

    private final LoginService loginService = mock(LoginService.class);
    private final AuthController authController = new AuthController(loginService);

    @Test
    void showLandingPageReturnsIndexView() {
        assertThat(authController.showLandingPage()).isEqualTo("index");
    }

    @Test
    void showLoginPageAddsLoginFormWhenMissing() {
        Model model = new ExtendedModelMap();

        String viewName = authController.showLoginPage(model);

        assertThat(viewName).isEqualTo("login");
        assertThat(model.asMap()).containsKey("loginForm");
    }

    @Test
    void processLoginReturnsLoginViewWhenValidationFails() {
        LoginForm loginForm = new LoginForm();
        BindingResult bindingResult = new BeanPropertyBindingResult(loginForm, "loginForm");
        bindingResult.rejectValue("username", "NotBlank");
        Model model = new ExtendedModelMap();

        String viewName = authController.processLogin(loginForm, bindingResult, model);

        assertThat(viewName).isEqualTo("login");
        verifyNoInteractions(loginService);
    }

    @Test
    void processLoginReturnsLoginViewWithErrorWhenCredentialsAreInvalid() {
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("admin");
        loginForm.setPassword("wrong");
        BindingResult bindingResult = new BeanPropertyBindingResult(loginForm, "loginForm");
        Model model = new ExtendedModelMap();
        when(loginService.authenticate("admin", "wrong")).thenReturn(false);

        String viewName = authController.processLogin(loginForm, bindingResult, model);

        assertThat(viewName).isEqualTo("login");
        assertThat(model.asMap()).containsEntry("loginError", "Invalid username or password");
        assertThat(model.asMap()).containsEntry("loginForm", loginForm);
        verify(loginService).authenticate("admin", "wrong");
    }

    @Test
    void processLoginReturnsWelcomeViewWhenCredentialsAreValid() {
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("admin");
        loginForm.setPassword("admin123");
        BindingResult bindingResult = new BeanPropertyBindingResult(loginForm, "loginForm");
        Model model = new ExtendedModelMap();
        when(loginService.authenticate("admin", "admin123")).thenReturn(true);

        String viewName = authController.processLogin(loginForm, bindingResult, model);

        assertThat(viewName).isEqualTo("shop");
        assertThat(model.asMap()).containsEntry("username", "admin");
        verify(loginService).authenticate("admin", "admin123");
    }

    @Test
    void processSignupRegistersCredentialsAndRedirectsToLogin() {
        SignupForm signupForm = new SignupForm();
        signupForm.setUsername("maker");
        signupForm.setPassword("tools123");
        BindingResult bindingResult = new BeanPropertyBindingResult(signupForm, "signupForm");
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String viewName = authController.processSignup(signupForm, bindingResult, redirectAttributes);

        assertThat(viewName).isEqualTo("redirect:/login");
        assertThat(redirectAttributes.getFlashAttributes().get("signupSuccess")).isEqualTo("Account created. Please sign in.");
        verify(loginService).register("maker", "tools123");
    }
}