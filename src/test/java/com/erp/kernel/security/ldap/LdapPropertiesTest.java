package com.erp.kernel.security.ldap;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link LdapProperties}.
 */
class LdapPropertiesTest {

    @Test
    void shouldGetAndSetAllProperties() {
        final var props = new LdapProperties();

        props.setEnabled(true);
        props.setUrl("ldap://localhost:389");
        props.setBaseDn("DC=erp,DC=com");
        props.setUserSearchFilter("(uid={0})");
        props.setBindDn("CN=admin,DC=erp,DC=com");
        props.setBindPassword("secret");

        assertThat(props.isEnabled()).isTrue();
        assertThat(props.getUrl()).isEqualTo("ldap://localhost:389");
        assertThat(props.getBaseDn()).isEqualTo("DC=erp,DC=com");
        assertThat(props.getUserSearchFilter()).isEqualTo("(uid={0})");
        assertThat(props.getBindDn()).isEqualTo("CN=admin,DC=erp,DC=com");
        assertThat(props.getBindPassword()).isEqualTo("secret");
    }

    @Test
    void shouldDefaultToDisabled() {
        final var props = new LdapProperties();

        assertThat(props.isEnabled()).isFalse();
    }
}
