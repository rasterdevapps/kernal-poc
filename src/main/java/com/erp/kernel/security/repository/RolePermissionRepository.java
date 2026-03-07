package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link RolePermission} entity persistence operations.
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * Finds all permission assignments for a role.
     *
     * @param roleId the role ID
     * @return the list of role-permission mappings
     */
    List<RolePermission> findByRoleId(Long roleId);

    /**
     * Checks whether a specific role-permission assignment exists.
     *
     * @param roleId       the role ID
     * @param permissionId the permission ID
     * @return {@code true} if the assignment exists
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
