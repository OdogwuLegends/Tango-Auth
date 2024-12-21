package com.tango.auth_service.repositories;

import com.tango.auth_service.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,String> {
}
