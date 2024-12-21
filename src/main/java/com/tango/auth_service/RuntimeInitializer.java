package com.tango.auth_service;

import com.tango.auth_service.entities.File;
import com.tango.auth_service.entities.Permission;
import com.tango.auth_service.entities.Role;
import com.tango.auth_service.entities.User;
import com.tango.auth_service.enums.AdminPermissions;
import com.tango.auth_service.enums.UserRole;
import com.tango.auth_service.enums.UserStatus;
import com.tango.auth_service.repositories.FileRepository;
import com.tango.auth_service.repositories.PermissionRepository;
import com.tango.auth_service.repositories.RoleRepository;
import com.tango.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RuntimeInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final FileRepository fileRepository;

//    private final String ADMIN_EMAIL = "super_30_admin@tango.com";
    private final String ADMIN_EMAIL = "emeralds161996@gmail.com";

    @Override
    public void run(String... args) throws Exception {
                    // JAVA
        // Git rebase / Git merge/ Merge conflict / Git stash (How do you pull something you've git stashed?)
        // Inner joins / database relationships
        // SOLID PRINCIPLES
        // Redis
        // Rabbit MQ
        // What are events in Spring boot? Listening for events etc
        // What processes would you use to populate the database with about 10 million records? Best practices (Asynchronously)
        // What types of Authentication - Username/password - JWT && OAuth
        // csrf
        // Difference between concurrency and latency
        // Dependency inversion
        // SQL injection / regression
        // Stored procedures
        // Difference between HashMap and HashTable
        // Order in Spring security
        // ACID
        // Explain MVC



            // Java
        // DO YOU WRITE UNIT TESTS?
        // Object class
        // Type promotion?
        // Overloading && Overriding  - Shadowing
        // Collections - Map, List etc
        // Java Streams?
        // Garbage collection
        // Threads (Smallest unit of a process) - Runnable || User threads (High Priority) || Daemon Threads
        // OOP - Pillars || Is Java 100% OOP?
        // Ways of implementing polymorphism - Method Overloading (Compile-Time Polymorphism) || Method Overriding (Run-Time Polymorphism) || Interface Implementation || Polymorphism with Generics
        // Compile time and Run time polymorphism
        // Marker interfaces and examples
        // Explain serialization & externalization
        // Exceptions - Try, catch, finally

        // What framework do you use and what build tool do you use? - Why do you use them?

            // Spring boot
        // Why do we use profiles in Spring boot?
        // Explain Dependency injection and how do you handle it in Spring boot
        // What is application context?
        // Tell me five Spring boot annotations and their functions - @RestController (@Component|| Class path scanning || Converts Response to JSON or XML)
        // Difference between RequestMapping and GetMapping
        // What does @Transactional do?
        // Spring boot actuator helps you monitor and manage your application on prod - auditing, health and other metrics


            //Security
        // How do you ensure that the client calling your backend application is legit?
        // Differentiate Authentication and Authorization
        // What types of Authentication - Username/password - JWT && OAuth
        // Five practices to enhance security of application || Authentication and Authorization, RBAC, Input validation, JWT, Encrypt sensitive data, Use HTTPS/SSL for Secure Communication
        // SQL injection
        // Explain CORS and how you handle it
        // Explain CSRF and how you handle it
        // Explain filters and an overview of Spring security


            // Microservices
        // Explain concept of Microservices
        // How do you handle application security in Microservices
        // Handling Asynchronous communication in Microservices


            // Database
        // ACID
        // What processes would you use to populate the database with about 10 million records? Best practices (Asynchronously)
        // Inner joins / database relationships
        // Database indexing
        // Database locking

            // Design patterns
        // SOLID PRINCIPLES

            // General
        // Besides REST, what other Architectural styles can data be exchanged by networks?
        // Are you familiar with SOAP and XML?
        // If you were to return a response to the FE that contains the sum, difference, average with a specific date, how would you write the code?
        // Difference between concurrency and latency
        // What ways would you improve the latency period of your application? Queries || Caching || DB indexing
        // Message brokers || Caching

                    // PYTHON
        // Talk about Database optimization


        if(!userRepository.existsByEmail(ADMIN_EMAIL)){
            createSuperUser();
        }

    }


    public void createSuperUser(){
        Role adminRole = createAdminRole(createSuperAdminPermissions());

        User superAdmin = User.builder()
                .name("SUPER ADMIN")
                .email(ADMIN_EMAIL)
                .phoneNumber("09089800900")
                .dateOfBirth(LocalDate.of(2010,10,15))
                .address("29, BERKLEY STREET, ONIKAN, LAGOS")
                .password(passwordEncoder.encode("helloWorld12@!"))
                .username("ADMIN_SUPER")
                .profilePicture(adminProfilePic())
                .userStatus(UserStatus.INDIVIDUAL)
                .userRole(adminRole)
                .isActive(true)
                .build();

        User savedSuperAdmin = userRepository.save(superAdmin);

        adminRole.setCreatedBy(savedSuperAdmin);
        roleRepository.save(adminRole);

        List<Permission> permissionList = permissionRepository.findAll();

        for (Permission permission : permissionList) {
            permission.setCreatedBy(savedSuperAdmin);
        }

        permissionRepository.saveAll(permissionList);
    }


    public Role createAdminRole(List<Permission> permission){
        Role adminRole = new Role();
        if(!roleRepository.existsByName(UserRole.SUPER_ADMIN.name())){
            adminRole = Role.builder()
                    .name(UserRole.SUPER_ADMIN.name())
                    .permission(permission)
                    .build();
        }
        return roleRepository.save(adminRole);
    }

    public List<Permission> createSuperAdminPermissions(){
        List<Permission> permissionsList = new ArrayList<>();

        for (AdminPermissions permissionEnum : AdminPermissions.values()) {
            boolean exists = permissionsList.stream()
                    .anyMatch(permission -> permission.getName().equals(permissionEnum.name()));

            if (exists) {
                continue;
            }

            Permission permission = new Permission();
            permission.setName(permissionEnum.name());
            permissionsList.add(permission);
        }
        return permissionRepository.saveAll(permissionsList);
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
