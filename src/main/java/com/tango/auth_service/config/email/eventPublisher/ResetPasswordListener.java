package com.tango.auth_service.config.email.eventPublisher;

import com.tango.auth_service.config.email.emailConfig.EmailService;
import com.tango.auth_service.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@AllArgsConstructor
@Slf4j
public class ResetPasswordListener implements ApplicationListener<ResetPasswordEvent> {
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {
        User user = event.getUser();
        String body = "Password Reset on Tango";

        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("name", user.getName());
        context.setVariable("otp", event.getOtp());

        final String htmlContent = templateEngine.process("reset-password", context);

        emailService.sendEmail(user.getEmail().toLowerCase(), body, htmlContent);
    }
}
