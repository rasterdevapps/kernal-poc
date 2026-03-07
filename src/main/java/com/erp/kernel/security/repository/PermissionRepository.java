package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link Permission} entity persistence operations.
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Finds a permission by name.
     *
     * @param permissionName the permission name
     * @return the permission if found
     */
    Optional<Permission> findByPermissionName(String permissionName);

    /**
     * Checks whether a permission exists with the given name.
     *
     * @param permissionName the permission name
     * @return {@code true} if a permission with the name exists
     */
    boolean existsByPermissionName(String permissionName);
}
