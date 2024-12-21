package com.tango.auth_service.repositories;


import com.tango.auth_service.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
    boolean existsByName(String name);

    Role findByName(String name);
}
