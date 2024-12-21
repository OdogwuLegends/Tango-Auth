package com.tango.auth_service.service.interfaces;


import com.tango.auth_service.entities.Otp;
import com.tango.auth_service.entities.User;

import java.util.Optional;

public interface OtpService {
    Otp create(Otp otp);
    Optional<Otp> findByOtpUserId(String userId);
    Optional<Otp> findByToken(String token);
    User findUserByOtpUserId(String otpUserId);
    boolean isOtpExpired(Otp otpEntity);
    boolean isOtpInvalid(Otp otpEntity, String otp);
}
