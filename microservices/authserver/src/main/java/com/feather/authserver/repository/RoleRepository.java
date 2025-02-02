package com.feather.authserver.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(FeatherRole name);

    Optional<Role> findByName(FeatherRole name);

}
