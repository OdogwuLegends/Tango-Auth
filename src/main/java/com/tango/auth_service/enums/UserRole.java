package com.tango.auth_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    SUPER_ADMIN("SUPER ADMIN"),SUB_ADMIN("SUB ADMIN"),
    CORPORATE("CORPORATE"),SUB_CORPORATE("SUB CORPORATE"),
    INDIVIDUAL("INDIVIDUAL"),SUB_INDIVIDUAL("SUB INDIVIDUAL");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @JsonCreator
    public static UserRole fromString(String value) {
        for (UserRole userRole : values()) {
            if (userRole.value.equals(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown USER ROLE: " + value);
    }
}
