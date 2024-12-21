package com.tango.auth_service.entities;

import com.tango.auth_service.config.utilsConfig.AttributeEncryptor;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.Instant;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "otp")
public class Otp extends BaseEntity{

    @Convert(converter = AttributeEncryptor.class)
    private String token;

    private String userId;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean expired;

    private Instant expiry;


    public Otp (String userId, Long expiry, String token) {
        this.token = token;
        this.userId = userId;
        this.expired = false;
        this.expiry = Instant.now().plus(Duration.ofSeconds(expiry));
    }
}
