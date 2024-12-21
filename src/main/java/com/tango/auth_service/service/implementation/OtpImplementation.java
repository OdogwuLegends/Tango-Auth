package com.tango.auth_service.service.implementation;


import com.tango.auth_service.entities.Otp;
import com.tango.auth_service.entities.User;
import com.tango.auth_service.repositories.OtpRepository;
import com.tango.auth_service.service.interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpImplementation implements OtpService {
    private final OtpRepository otpRepository;

    @Override
    public Otp create(Otp otp) {
        return otpRepository.save(otp);
    }

    @Override
    public Optional<Otp> findByOtpUserId(String userId) {
        return otpRepository.findByUserIdAndExpiredFalse(userId);
    }

    @Override
    public Optional<Otp> findByToken(String token) {
        return otpRepository.findByToken(token);
    }

    @Override
    public User findUserByOtpUserId(String otpUserId) {
        return otpRepository.findUserByOtpUserId(otpUserId).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public boolean isOtpExpired(Otp otpEntity) {
        return otpEntity.isExpired() || Instant.now().isAfter(otpEntity.getExpiry());
    }

    @Override
    public boolean isOtpInvalid(Otp otpEntity, String otp) {
        return !otpEntity.getToken().equals(otp);
    }


}
