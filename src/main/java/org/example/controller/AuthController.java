package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.login.RefreshTokenRequestDto;
import org.example.dto.user.login.UserLoginRequestDto;
import org.example.dto.user.login.UserLoginResponseDto;
import org.example.dto.user.password.ForgotPasswordRequestDto;
import org.example.dto.user.password.ResetPasswordRequestDto;
import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.exception.RegistrationException;
import org.example.service.user.authentication.AuthenticationServiceImpl;
import org.example.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentification management",
        description = "Endpoints for managing user registration and authentification")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "User registration",
            description = "Create a new user")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "User authentication",
            description = "Authenticate an existing user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping("/confirm-email")
    @Operation(summary = "Confirm email",
            description = "Activates user account via email token")
    public void confirmEmail(@RequestParam("token") String token) {
        userService.confirmEmail(token);
    }

    @GetMapping("/resend-verification")
    @Operation(summary = "Resend verification link",
            description = "Resend email verification link by user's email")
    public void resendEmail(@RequestParam("email") String email) {
        userService.resendEmail(email);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Forgot password",
            description = "Initiates password recovery process")
    public void forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto request) {
        userService.processForgotPassword(request.getEmail());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reset password",
            description = "Sets a new password using a recovery token")
    public void resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token",
            description = "Get a new access token using a valid refresh token")
    public UserLoginResponseDto refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        return authenticationService.refreshToken(request);
    }
}
