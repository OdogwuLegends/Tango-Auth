package com.tango.auth_service.config.email.eventPublisher;

import com.tango.auth_service.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    private void publishSignUpEvent(final User user, final String password) {
        SignUpEvent signUpEvent = new SignUpEvent(this, user, password);
        applicationEventPublisher.publishEvent(signUpEvent);
    }

    private void publishPasswordResetEvent(final User user, final String otp) {
        ResetPasswordEvent resetPasswordEvent = new ResetPasswordEvent(this, user, otp);
        applicationEventPublisher.publishEvent(resetPasswordEvent);
    }

    public void sendSignUpEmail(User user, String password) {
        CompletableFuture.runAsync(() -> {
            publishSignUpEvent(user, password);
        });
    }

    public void sendPasswordResetEmail(User user, String otp) {
        CompletableFuture.runAsync(() -> {
            publishPasswordResetEvent(user, otp);
        });
    }
}
