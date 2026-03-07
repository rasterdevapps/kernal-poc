package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.LoginPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link LoginPolicy} entity persistence operations.
 */
public interface LoginPolicyRepository extends JpaRepository<LoginPolicy, Long> {

    /**
     * Finds a login policy by name.
     *
     * @param policyName the policy name
     * @return the policy if found
     */
    Optional<LoginPolicy> findByPolicyName(String policyName);

    /**
     * Checks whether a login policy exists with the given name.
     *
     * @param policyName the policy name
     * @return {@code true} if a policy with the name exists
     */
    boolean existsByPolicyName(String policyName);
}
