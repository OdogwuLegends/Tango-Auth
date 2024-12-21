package com.tango.auth_service.config.email.eventPublisher;


import com.tango.auth_service.entities.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignUpEvent extends ApplicationEvent {
    private final User user;
    private final String password;

    public SignUpEvent(Object source, User user, String password) {
        super(source);
        this.user = user;
        this.password = password;
    }
}
