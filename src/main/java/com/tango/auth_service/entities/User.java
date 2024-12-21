package com.tango.auth_service.entities;

import com.tango.auth_service.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(unique = true, nullable = false,updatable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; // Card , Vehicle, User

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @JoinColumn(nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private File profilePicture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Role userRole;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isActive;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean hasChangedDefaultPassword;
}
