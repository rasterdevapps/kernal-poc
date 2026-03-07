package com.erp.kernel.security.service;

import com.erp.kernel.security.dto.MfaSetupResponse;
import com.erp.kernel.security.entity.MfaConfiguration;
import com.erp.kernel.security.repository.MfaConfigurationRepository;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service managing multi-factor authentication (MFA) configuration and verification.
 *
 * <p>Handles TOTP-based 2FA setup, enrollment, and code verification for users.
 */
@Service
@Transactional(readOnly = true)
public class MfaService {

    private static final Logger LOG = LoggerFactory.getLogger(MfaService.class);
    private static final String MFA_TYPE_TOTP = "TOTP";
    private static final String ISSUER = "ERP-Kernel";

    private final MfaConfigurationRepository mfaConfigurationRepository;
    private final TotpService totpService;

    /**
     * Creates a new MFA service.
     *
     * @param mfaConfigurationRepository the MFA configuration repository
     * @param totpService                the TOTP service
     */
    public MfaService(final MfaConfigurationRepository mfaConfigurationRepository,
                      final TotpService totpService) {
        this.mfaConfigurationRepository = Objects.requireNonNull(mfaConfigurationRepository,
                "mfaConfigurationRepository must not be null");
        this.totpService = Objects.requireNonNull(totpService, "totpService must not be null");
    }

    /**
     * Sets up TOTP-based MFA for a user.
     *
     * @param userId   the user ID
     * @param username the username for provisioning URI
     * @return the MFA setup response with secret and provisioning URI
     */
    @Transactional
    public MfaSetupResponse setupTotp(final Long userId, final String username) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(username, "username must not be null");

        final var secretKey = totpService.generateSecret();
        final var provisioningUri = totpService.generateProvisioningUri(username, ISSUER, secretKey);

        final var config = mfaConfigurationRepository
                .findByUserIdAndMfaType(userId, MFA_TYPE_TOTP)
                .orElseGet(MfaConfiguration::new);

        config.setUserId(userId);
        config.setMfaType(MFA_TYPE_TOTP);
        config.setSecretKey(secretKey);
        config.setEnabled(false);
        config.setVerified(false);
        mfaConfigurationRepository.save(config);

        LOG.info("TOTP MFA setup initiated for user ID {}", userId);
        return new MfaSetupResponse(secretKey, provisioningUri);
    }

    /**
     * Verifies a TOTP code and enables MFA for the user.
     *
     * @param userId the user ID
     * @param code   the TOTP code to verify
     * @return {@code true} if the code is valid and MFA is enabled
     */
    @Transactional
    public boolean verifyAndEnable(final Long userId, final String code) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(code, "code must not be null");

        final var config = mfaConfigurationRepository
                .findByUserIdAndMfaType(userId, MFA_TYPE_TOTP)
                .orElseThrow(() -> new EntityNotFoundException(
                        "MFA configuration not found for user ID %d".formatted(userId)));

        if (totpService.validateCode(code, config.getSecretKey())) {
            config.setVerified(true);
            config.setEnabled(true);
            mfaConfigurationRepository.save(config);
            LOG.info("TOTP MFA enabled for user ID {}", userId);
            return true;
        }

        LOG.warn("TOTP verification failed for user ID {}", userId);
        return false;
    }

    /**
     * Validates a TOTP code for an authenticated user.
     *
     * @param userId the user ID
     * @param code   the TOTP code
     * @return {@code true} if the code is valid
     */
    public boolean validateCode(final Long userId, final String code) {
        Objects.requireNonNull(userId, "userId must not be null");

        if (code == null) {
            return false;
        }

        final var configOptional = mfaConfigurationRepository
                .findByUserIdAndMfaType(userId, MFA_TYPE_TOTP);

        if (configOptional.isEmpty() || !configOptional.get().isEnabled()) {
            return true;
        }

        return totpService.validateCode(code, configOptional.get().getSecretKey());
    }

    /**
     * Checks whether MFA is enabled for a user.
     *
     * @param userId the user ID
     * @return {@code true} if MFA is enabled
     */
    public boolean isMfaEnabled(final Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return mfaConfigurationRepository
                .findByUserIdAndMfaType(userId, MFA_TYPE_TOTP)
                .map(config -> config.isEnabled() && config.isVerified())
                .orElse(false);
    }
}
