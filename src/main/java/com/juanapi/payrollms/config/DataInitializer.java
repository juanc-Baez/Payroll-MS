package com.juanapi.payrollms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.juanapi.payrollms.model.Role;
import com.juanapi.payrollms.repository.RoleRepo;
import com.juanapi.payrollms.service.UserService;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner{
    
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists("ADMIN");;
        createRoleIfNotExists("HR");
        createRoleIfNotExists("EMPLEADO");
    // Seed admin user if not exists
    userService.createUser("admin", "admin123", Set.of("ADMIN"));
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepo.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            roleRepo.save(role);
        }
    }
}