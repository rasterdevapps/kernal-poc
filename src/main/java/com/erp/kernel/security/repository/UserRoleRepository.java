package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link UserRole} entity persistence operations.
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * Finds all role assignments for a user.
     *
     * @param userId the user ID
     * @return the list of user-role mappings
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * Checks whether a specific user-role assignment exists.
     *
     * @param userId the user ID
     * @param roleId the role ID
     * @return {@code true} if the assignment exists
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * Deletes all role assignments for a user.
     *
     * @param userId the user ID
     */
    void deleteByUserId(Long userId);
}
