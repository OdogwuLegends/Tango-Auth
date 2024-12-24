package com.tango.auth_service.config.rabbitMq;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RabbitUserDto {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String otp;
    private EventType eventType;
}
