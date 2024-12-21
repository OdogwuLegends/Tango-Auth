package com.tango.auth_service.repositories;


import com.tango.auth_service.entities.Otp;
import com.tango.auth_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByUserIdAndExpiredFalse(String userId);

    Optional<Otp> findByToken(String token);

    @Query("SELECT u FROM User u WHERE u.id = (SELECT o.userId FROM Otp o WHERE o.userId = :userId)")
    Optional<User> findUserByOtpUserId(@Param("userId") String userId);
}
