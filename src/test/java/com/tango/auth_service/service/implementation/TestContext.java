package com.tango.auth_service.service.implementation;

import org.springframework.stereotype.Component;

@Component
public class TestContext {
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}

