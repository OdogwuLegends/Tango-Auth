package com.tango.auth_service.service.implementation;


import com.fasterxml.jackson.databind.util.BeanUtil;
import com.tango.auth_service.config.email.eventPublisher.EventPublisher;
import com.tango.auth_service.config.jwt.JwtService;
import com.tango.auth_service.config.rabbitMq.EventType;
import com.tango.auth_service.config.rabbitMq.MQConfig;
import com.tango.auth_service.config.rabbitMq.RabbitUserDto;
import com.tango.auth_service.config.springSecurity.UserDetailsImplementation;
import com.tango.auth_service.dtos.LoginDto;
import com.tango.auth_service.dtos.PasswordDto;
import com.tango.auth_service.entities.File;
import com.tango.auth_service.entities.Otp;
import com.tango.auth_service.entities.Role;
import com.tango.auth_service.entities.User;
import com.tango.auth_service.enums.UserRole;
import com.tango.auth_service.enums.UserStatus;
import com.tango.auth_service.repositories.FileRepository;
import com.tango.auth_service.repositories.PermissionRepository;
import com.tango.auth_service.repositories.RoleRepository;
import com.tango.auth_service.repositories.UserRepository;
import com.tango.auth_service.service.interfaces.AuthService;
import com.tango.auth_service.service.interfaces.OtpService;
import com.tango.auth_service.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import static com.tango.auth_service.utils.AppUtils.generateNumericOTP;
import static com.tango.auth_service.utils.ResponseUtils.createFailureResponse;
import static com.tango.auth_service.utils.ResponseUtils.createSuccessResponse;


@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final FileRepository fileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<ApiResponse<?>> createUser() {
        String email = "odigbocharlesjnrlegends@gmail.com";
        String plainPassword = "helloWorld12@!";
        Role adminRole = roleRepository.findByName(UserRole.SUPER_ADMIN.name());

        User superAdmin = User.builder()
                .name("SECOND_SUPER ADMIN")
                .email(email)
                .phoneNumber("09089800901")
                .dateOfBirth(LocalDate.of(2010,10,15))
                .address("29, BERKLEY STREET, ONIKAN, LAGOS")
                .password(passwordEncoder.encode(plainPassword))
                .username("SECOND_ADMIN_SUPER")
                .profilePicture(adminProfilePic())
                .userStatus(UserStatus.INDIVIDUAL)
                .userRole(adminRole)
                .isActive(true)
                .build();

        User savedSuperAdmin = userRepository.save(superAdmin);

        RabbitUserDto rabbitUserDto = new RabbitUserDto();
        BeanUtils.copyProperties(savedSuperAdmin,rabbitUserDto);
        rabbitUserDto.setPassword(plainPassword);
        rabbitUserDto.setEventType(EventType.SIGN_UP);
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, rabbitUserDto);

        return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse("","User sign up successful"));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> login(LoginDto.Request request) {
        try {
            User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim()).orElseThrow(() -> new BadCredentialsException("User not found"));

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(),request.getPassword()));
            UserDetails authenticatedUser = new UserDetailsImplementation(user);

            String jwToken = jwtService.generateToken(authenticatedUser);

            LoginDto.Response response = LoginDto.Response.builder()
                    .userId(user.getId())
                    .userRole(user.getUserRole().getName())
                    .hasChangedDefaultPassword(user.isHasChangedDefaultPassword())
                    .jwToken(jwToken)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse(response,"Login successful"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createFailureResponse("Login failed", "Bad credentials"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse("Login failed", ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> changePassword(Principal principal, PasswordDto.ChangePassword request) {
        User authenticatedUser = userRepository.findByEmail(principal.getName().toLowerCase().trim()).orElseThrow(() -> new BadCredentialsException("User not found"));

        if(!passwordEncoder.matches(request.getCurrentPassword(), authenticatedUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse("Invalid credentials", "Password change failed"));
        }

        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse("Invalid credentials", "New passwords do not match"));
        }

        authenticatedUser.setPassword(passwordEncoder.encode(request.getConfirmNewPassword()));

        if(!authenticatedUser.isHasChangedDefaultPassword()){
            authenticatedUser.setHasChangedDefaultPassword(true);
        }

        userRepository.save(authenticatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse("Operation successful","Password change successful"));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> initiatePasswordReset(PasswordDto.InitiatePasswordReset request) {
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException(" not correct"));
            String numericOTP = generateNumericOTP();
            System.err.println("numericOTP:: " + numericOTP);
            RabbitUserDto rabbitUserDto = new RabbitUserDto();
            BeanUtils.copyProperties(user,rabbitUserDto);
            rabbitUserDto.setOtp(numericOTP);
            rabbitUserDto.setEventType(EventType.RESET_PASSWORD);
            rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, rabbitUserDto);

            final Long TIME_IN_SECONDS =  3600L; // 1 hour
            Otp otp = new Otp(user.getId(),TIME_IN_SECONDS,numericOTP);
            otpService.create(otp);

            return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse("Operation successful",String.format("OTP sent to %s ",user.getEmail())));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createFailureResponse("Operation failed", "Email not correct"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse("Operation failed", ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> completePasswordReset(PasswordDto.CompletePasswordReset request) {
        try {
            Optional<Otp> otp = otpService.findByToken(request.getToken());

            if(otp.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createFailureResponse("Operation failed", "Invalid OTP"));
            }

            User user = otpService.findUserByOtpUserId(otp.get().getUserId());
            Optional<Otp> OptionalOtp = otpService.findByOtpUserId(user.getId());

            if(OptionalOtp.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createFailureResponse("OTP not found", "OTP not found"));
            }

            Otp otpEntity = OptionalOtp.get();

            if(otpService.isOtpExpired(otpEntity)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createFailureResponse("Expired OTP", "OTP expired. Please request a new one."));
            }

            if(otpService.isOtpInvalid(otpEntity, request.getToken())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createFailureResponse("Invalid OTP", "OTP incorrect."));
            }

            if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse("Bad credentials", "New passwords do not match"));
            }

            user.setPassword(passwordEncoder.encode(request.getConfirmNewPassword()));
            userRepository.save(user);

            otpEntity.setExpired(true);
            otpService.create(otpEntity);

            return ResponseEntity.status(HttpStatus.OK).body(createSuccessResponse("Operation successful","Password Reset completed"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createFailureResponse("Operation failed", ex.getMessage()));
        }
    }

    public File adminProfilePic(){
        File file = File.builder()
                .name("SUPER_ADMIN")
                .extension("N/A")
                .originalName("SUPER_ADMIN")
                .size("N/A")
                .build();
        return fileRepository.save(file);
    }
}
