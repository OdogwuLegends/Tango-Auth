package com.tango.auth_service.utils;

import java.util.Random;

public class AppUtils {

    public static String generateNumericOTP() {
        Random random = new Random();
        int otpLength = 6;
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }
}
