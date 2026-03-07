package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link Role} entity persistence operations.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by name.
     *
     * @param roleName the role name
     * @return the role if found
     */
    Optional<Role> findByRoleName(String roleName);

    /**
     * Checks whether a role exists with the given name.
     *
     * @param roleName the role name
     * @return {@code true} if a role with the name exists
     */
    boolean existsByRoleName(String roleName);
}
