package com.jitendra.marketplace.auth.service;

import com.jitendra.marketplace.auth.model.Role;
import com.jitendra.marketplace.auth.model.RoleName;
import com.jitendra.marketplace.auth.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedRole(RoleName.ROLE_BUYER);
        seedRole(RoleName.ROLE_SUPPLIER);
    }

    private void seedRole(RoleName roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
