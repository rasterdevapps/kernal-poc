package com.erp.kernel.security.ldap;

import com.erp.kernel.security.auth.AuthenticationProvider;
import com.erp.kernel.security.auth.AuthenticationProviderType;
import com.erp.kernel.security.auth.AuthenticationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Authentication provider for LDAP/Active Directory.
 *
 * <p>Authenticates users against an LDAP directory when LDAP integration
 * is enabled. Falls back to local authentication when LDAP is disabled.
 */
@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(LdapAuthenticationProvider.class);

    private final LdapProperties ldapProperties;

    /**
     * Creates a new LDAP authentication provider.
     *
     * @param ldapProperties the LDAP configuration properties
     */
    public LdapAuthenticationProvider(final LdapProperties ldapProperties) {
        this.ldapProperties = Objects.requireNonNull(ldapProperties, "ldapProperties must not be null");
    }

    @Override
    public AuthenticationProviderType getType() {
        return AuthenticationProviderType.LDAP;
    }

    @Override
    public AuthenticationResult authenticate(final String username, final String credential) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(credential, "credential must not be null");

        if (!ldapProperties.isEnabled()) {
            LOG.debug("LDAP authentication skipped: LDAP is disabled");
            return AuthenticationResult.failure(username, AuthenticationProviderType.LDAP,
                    "LDAP authentication is not enabled");
        }

        LOG.info("Attempting LDAP authentication for user '{}' against '{}'",
                username, ldapProperties.getUrl());

        // LDAP bind authentication would be performed here using the configured
        // LDAP URL, base DN, and search filter. For the PoC, the framework is
        // established and ready for integration with a real LDAP server.
        return AuthenticationResult.failure(username, AuthenticationProviderType.LDAP,
                "LDAP server connection not configured for this environment");
    }

    @Override
    public boolean supports(final AuthenticationProviderType type) {
        return AuthenticationProviderType.LDAP == type;
    }
}
