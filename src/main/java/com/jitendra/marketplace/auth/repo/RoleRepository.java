package com.jitendra.marketplace.auth.repo;

import com.jitendra.marketplace.auth.model.Role;
import com.jitendra.marketplace.auth.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
