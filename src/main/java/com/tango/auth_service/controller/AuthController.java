package com.tango.auth_service.controller;


import com.tango.auth_service.config.email.emailConfig.EmailService;
import com.tango.auth_service.dtos.LoginDto;
import com.tango.auth_service.dtos.PasswordDto;
import com.tango.auth_service.service.interfaces.AuthService;
import com.tango.auth_service.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@Valid @RequestBody LoginDto.Request request){
        return authService.login(request);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(Principal principal, @Valid @RequestBody PasswordDto.ChangePassword request){
        return authService.changePassword(principal, request);
    }

    @PostMapping("/initiate-password-reset")
    public ResponseEntity<ApiResponse<?>> initiatePasswordReset(@Valid @RequestBody PasswordDto.InitiatePasswordReset request){
        return authService.initiatePasswordReset(request);
    }

    @PostMapping("/complete-password-reset")
    public ResponseEntity<ApiResponse<?>> completePasswordReset(@Valid @RequestBody  PasswordDto.CompletePasswordReset request){
        return authService.completePasswordReset(request);
    }

}
