package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.MfaConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link MfaConfiguration} entity persistence operations.
 */
public interface MfaConfigurationRepository extends JpaRepository<MfaConfiguration, Long> {

    /**
     * Finds the MFA configuration for a user and type.
     *
     * @param userId  the user ID
     * @param mfaType the MFA type (e.g., TOTP)
     * @return the MFA configuration if found
     */
    Optional<MfaConfiguration> findByUserIdAndMfaType(Long userId, String mfaType);

    /**
     * Checks whether MFA is configured for a user with a specific type.
     *
     * @param userId  the user ID
     * @param mfaType the MFA type
     * @return {@code true} if MFA is configured
     */
    boolean existsByUserIdAndMfaType(Long userId, String mfaType);
}
