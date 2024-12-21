package com.tango.auth_service.repositories;


import com.tango.auth_service.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,String> {
    boolean existsByName(String name);
}
