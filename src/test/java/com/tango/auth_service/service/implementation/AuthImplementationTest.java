package com.tango.auth_service.service.implementation;

import com.tango.auth_service.dtos.LoginDto;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
    void changePassword() {
    }

    @Test
    void initiatePasswordReset() {
    }

    @Test
    void completePasswordReset() {
    }
}