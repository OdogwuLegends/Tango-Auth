package com.tango.auth_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
//        @Email(message = "Email should be valid")
//        @NotBlank(message = "email is required")
//        private String email;

        @NotBlank(message = "username is required")
        private String username;

        @NotBlank(message = "password is required")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String userRole;
        private String userId;
        private boolean hasChangedDefaultPassword;
        private String jwToken;
    }
}
