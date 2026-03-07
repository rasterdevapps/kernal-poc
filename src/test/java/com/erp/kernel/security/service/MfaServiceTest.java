package com.erp.kernel.security.service;

import com.erp.kernel.security.entity.MfaConfiguration;
import com.erp.kernel.security.repository.MfaConfigurationRepository;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link MfaService}.
 */
@ExtendWith(MockitoExtension.class)
class MfaServiceTest {

    @Mock
    private MfaConfigurationRepository mfaConfigurationRepository;

    @Mock
    private TotpService totpService;

    @InjectMocks
    private MfaService mfaService;

    @Test
    void shouldSetupTotp_whenNoExistingConfig() {
        when(totpService.generateSecret()).thenReturn("SECRET123");
        when(totpService.generateProvisioningUri("user", "ERP-Kernel", "SECRET123"))
                .thenReturn("otpauth://totp/ERP-Kernel:user?secret=SECRET123");
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.empty());
        when(mfaConfigurationRepository.save(any(MfaConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = mfaService.setupTotp(1L, "user");

        assertThat(result.secretKey()).isEqualTo("SECRET123");
        assertThat(result.provisioningUri()).contains("otpauth://");
        verify(mfaConfigurationRepository).save(any(MfaConfiguration.class));
    }

    @Test
    void shouldSetupTotp_whenExistingConfigExists() {
        final var existingConfig = new MfaConfiguration();
        existingConfig.setUserId(1L);
        existingConfig.setMfaType("TOTP");
        when(totpService.generateSecret()).thenReturn("NEWSECRET");
        when(totpService.generateProvisioningUri("user", "ERP-Kernel", "NEWSECRET"))
                .thenReturn("otpauth://totp/ERP-Kernel:user?secret=NEWSECRET");
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(existingConfig));
        when(mfaConfigurationRepository.save(any(MfaConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = mfaService.setupTotp(1L, "user");

        assertThat(result.secretKey()).isEqualTo("NEWSECRET");
    }

    @Test
    void shouldVerifyAndEnable_whenCodeIsValid() {
        final var config = createMfaConfig(1L, "SECRET", false, false);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));
        when(totpService.validateCode("123456", "SECRET")).thenReturn(true);
        when(mfaConfigurationRepository.save(any(MfaConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = mfaService.verifyAndEnable(1L, "123456");

        assertThat(result).isTrue();
        assertThat(config.isEnabled()).isTrue();
        assertThat(config.isVerified()).isTrue();
    }

    @Test
    void shouldNotEnable_whenCodeIsInvalid() {
        final var config = createMfaConfig(1L, "SECRET", false, false);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));
        when(totpService.validateCode("000000", "SECRET")).thenReturn(false);

        final var result = mfaService.verifyAndEnable(1L, "000000");

        assertThat(result).isFalse();
    }

    @Test
    void shouldThrowEntityNotFoundException_whenVerifyingWithNoConfig() {
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> mfaService.verifyAndEnable(1L, "123456"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldValidateCode_whenMfaIsEnabled() {
        final var config = createMfaConfig(1L, "SECRET", true, true);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));
        when(totpService.validateCode("123456", "SECRET")).thenReturn(true);

        assertThat(mfaService.validateCode(1L, "123456")).isTrue();
    }

    @Test
    void shouldReturnTrue_whenMfaIsNotEnabled() {
        final var config = createMfaConfig(1L, "SECRET", false, false);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));

        assertThat(mfaService.validateCode(1L, "123456")).isTrue();
    }

    @Test
    void shouldReturnTrue_whenNoMfaConfigExists() {
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.empty());

        assertThat(mfaService.validateCode(1L, "123456")).isTrue();
    }

    @Test
    void shouldReturnFalse_whenCodeIsNullAndMfaNotConfigured() {
        assertThat(mfaService.validateCode(1L, null)).isFalse();
    }

    @Test
    void shouldCheckMfaEnabled_whenEnabledAndVerified() {
        final var config = createMfaConfig(1L, "SECRET", true, true);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));

        assertThat(mfaService.isMfaEnabled(1L)).isTrue();
    }

    @Test
    void shouldReturnFalse_whenMfaNotVerified() {
        final var config = createMfaConfig(1L, "SECRET", true, false);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));

        assertThat(mfaService.isMfaEnabled(1L)).isFalse();
    }

    @Test
    void shouldReturnFalse_whenMfaNotEnabled() {
        final var config = createMfaConfig(1L, "SECRET", false, true);
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.of(config));

        assertThat(mfaService.isMfaEnabled(1L)).isFalse();
    }

    @Test
    void shouldReturnFalse_whenMfaNotConfigured() {
        when(mfaConfigurationRepository.findByUserIdAndMfaType(1L, "TOTP"))
                .thenReturn(Optional.empty());

        assertThat(mfaService.isMfaEnabled(1L)).isFalse();
    }

    private MfaConfiguration createMfaConfig(final Long userId, final String secret,
                                              final boolean enabled, final boolean verified) {
        final var config = new MfaConfiguration();
        config.setUserId(userId);
        config.setMfaType("TOTP");
        config.setSecretKey(secret);
        config.setEnabled(enabled);
        config.setVerified(verified);
        return config;
    }
}
