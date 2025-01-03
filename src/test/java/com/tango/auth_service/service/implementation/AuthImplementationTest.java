package com.tango.auth_service.service.implementation;

import com.tango.auth_service.dtos.LoginDto;
import com.tango.auth_service.dtos.PasswordDto;
import com.tango.auth_service.entities.User;
import com.tango.auth_service.repositories.UserRepository;
import com.tango.auth_service.service.interfaces.AuthService;
import com.tango.auth_service.utils.ApiResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback(false)
class AuthImplementationTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestContext testContext;



    @Test
    @Order(1)
    void createUser() {
        ResponseEntity<ApiResponse<?>> response = authService.createUser();
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Optional<User> userOptional = userRepository.findByEmail("odigbocharlesjnrlegends@gmail.com");
        assertTrue(userOptional.isPresent(), "User should be present in the database");
        User user = userOptional.get();
        assertEquals("09089800901", user.getPhoneNumber());
    }


    @Test
    @Order(2)
    void loginWithEmail() {
        LoginDto.Request request = new LoginDto.Request("odigbocharlesjnrlegends@gmail.com","helloWorld12@!");
        ResponseEntity<ApiResponse<?>> responseEntity = authService.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<?> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse, "ApiResponse should not be null");

        assertEquals("Login successful", apiResponse.getMessage());

        LoginDto.Response loginResponse = (LoginDto.Response) apiResponse.getData();
        assertNotNull(loginResponse, "Data field should not be null");

        assertNotNull(loginResponse.getJwToken(), "JWT Token should not be null");
        assertFalse(loginResponse.isHasChangedDefaultPassword());
    }

    @Test
    @Order(3)
    void loginWithPhoneNumber() {
        LoginDto.Request request = new LoginDto.Request("09089800901","helloWorld12@!");
        ResponseEntity<ApiResponse<?>> responseEntity = authService.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<?> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse, "ApiResponse should not be null");

        assertEquals("Login successful", apiResponse.getMessage());

        LoginDto.Response loginResponse = (LoginDto.Response) apiResponse.getData();
        assertNotNull(loginResponse, "Data field should not be null");

        assertNotNull(loginResponse.getJwToken(), "JWT Token should not be null");
        assertFalse(loginResponse.isHasChangedDefaultPassword());
    }


    @Test
    @Order(4)
    void changePassword() {
        Principal mockPrincipal = () -> "odigbocharlesjnrlegends@gmail.com";
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockPrincipal, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginDto.Request loginRequest = new LoginDto.Request("09089800901", "helloWorld12@!");
        ResponseEntity<ApiResponse<?>> LoginResponse = authService.login(loginRequest);

        assertEquals(HttpStatus.OK, LoginResponse.getStatusCode());

        PasswordDto.ChangePassword changePasswordRequest = new PasswordDto.ChangePassword();
        changePasswordRequest.setCurrentPassword("helloWorld12@!");
        changePasswordRequest.setNewPassword("Legendary96$");
        changePasswordRequest.setConfirmNewPassword("Legendary96$");

        ResponseEntity<ApiResponse<?>> changePasswordResponse = authService.changePassword(mockPrincipal, changePasswordRequest);

        assertEquals(HttpStatus.OK, changePasswordResponse.getStatusCode());

        ApiResponse<?> apiResponse = changePasswordResponse.getBody();
        assertNotNull(apiResponse, "ApiResponse should not be null");
        assertEquals("Password change successful", apiResponse.getMessage());
    }



    @Test
    @Order(5)
    void initiatePasswordReset() {
        PasswordDto.InitiatePasswordReset request = new PasswordDto.InitiatePasswordReset("odigbocharlesjnrlegends@gmail.com");
        ResponseEntity<ApiResponse<?>> passwordResetResponse = authService.initiatePasswordReset(request);
        assertEquals(HttpStatus.OK, passwordResetResponse.getStatusCode());

        ApiResponse<?> responseBody = passwordResetResponse.getBody();
        assertNotNull(responseBody);

        String numericOTP = (String) responseBody.getData();
        assertNotNull(numericOTP);
        testContext.setOtp(numericOTP);

        System.out.println("Extracted OTP: " + numericOTP);

        assertEquals("Operation successful", responseBody.getMessage());
        assertTrue(responseBody.isStatus());
    }

    @Test
    @Order(6)
    void completePasswordReset() {
        System.out.println("Using OTP: "+testContext.getOtp());
        String otp = testContext.getOtp();
        assertNotNull(otp, "OTP should not be null. Make sure initiatePasswordReset has run successfully.");

        PasswordDto.CompletePasswordReset request = new PasswordDto.CompletePasswordReset();
        request.setToken(otp);
        request.setNewPassword("Legends16@");
        request.setConfirmNewPassword("Legends16@");

        ResponseEntity<ApiResponse<?>> response = authService.completePasswordReset(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<?> apiResponseBody = response.getBody();
        assertNotNull(apiResponseBody, "ApiResponse should not be null");
        assertTrue(apiResponseBody.isStatus(), "ApiResponse status should be true");
        assertEquals("Operation successful", apiResponseBody.getData());
        assertEquals("Password Reset completed", apiResponseBody.getMessage());
    }

}