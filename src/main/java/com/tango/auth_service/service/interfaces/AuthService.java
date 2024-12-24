package com.tango.auth_service.service.interfaces;


import com.tango.auth_service.dtos.LoginDto;
import com.tango.auth_service.dtos.PasswordDto;
import com.tango.auth_service.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface AuthService {
    ResponseEntity<ApiResponse<?>> createUser();
    ResponseEntity<ApiResponse<?>> login(LoginDto.Request request);
    ResponseEntity<ApiResponse<?>> changePassword(Principal principal, PasswordDto.ChangePassword request);
    ResponseEntity<ApiResponse<?>> initiatePasswordReset(PasswordDto.InitiatePasswordReset request);
    ResponseEntity<ApiResponse<?>> completePasswordReset(PasswordDto.CompletePasswordReset request);
}
