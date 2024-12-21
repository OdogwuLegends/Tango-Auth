package com.tango.auth_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class PasswordDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePassword{
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\\\-_=+\\\\[\\]{}|;:'\\\",.<>?/]])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and include alphanumeric characters and special characters")
        private String newPassword;


        @NotBlank(message = "Password confirmation is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\\\-_=+\\\\[\\]{}|;:'\\\",.<>?/]])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and include alphanumeric characters and special characters")
        private String confirmNewPassword;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InitiatePasswordReset{
        @NotBlank(message = "Email is required")
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompletePasswordReset{
        @NotBlank(message = "OTP is required")
        private String token;

        @NotBlank(message = "New password is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\\\-_=+\\\\[\\]{}|;:'\\\",.<>?/]])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and include alphanumeric characters and special characters")
        private String newPassword;

        @NotBlank(message = "Password confirmation is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\\\-_=+\\\\[\\]{}|;:'\\\",.<>?/]])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and include alphanumeric characters and special characters")
        private String confirmNewPassword;
    }
}
