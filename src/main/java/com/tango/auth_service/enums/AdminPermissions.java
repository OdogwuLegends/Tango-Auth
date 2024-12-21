package com.tango.auth_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AdminPermissions {
    CREATE("CREATE"), EDIT("EDIT");

    private final String permission;

    AdminPermissions(String permission) {
        this.permission = permission;
    }

    @JsonCreator
    public static AdminPermissions fromString(String permission) {
        for (AdminPermissions value : AdminPermissions.values()) {
            if (value.permission.equals(permission)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown USER permission: " + permission);
    }
}
