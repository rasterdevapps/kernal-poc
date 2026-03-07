package com.erp.kernel.security.ldap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for LDAP/Active Directory integration.
 *
 * <p>Binds to properties prefixed with {@code erp.security.ldap}.
 */
@Component
@ConfigurationProperties(prefix = "erp.security.ldap")
public class LdapProperties {

    private boolean enabled;
    private String url;
    private String baseDn;
    private String userSearchFilter;
    private String bindDn;
    private String bindPassword;

    /**
     * Returns whether LDAP integration is enabled.
     *
     * @return {@code true} if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether LDAP integration is enabled.
     *
     * @param enabled the enabled flag
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the LDAP server URL.
     *
     * @return the LDAP URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the LDAP server URL.
     *
     * @param url the LDAP URL
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Returns the base distinguished name for user searches.
     *
     * @return the base DN
     */
    public String getBaseDn() {
        return baseDn;
    }

    /**
     * Sets the base distinguished name for user searches.
     *
     * @param baseDn the base DN
     */
    public void setBaseDn(final String baseDn) {
        this.baseDn = baseDn;
    }

    /**
     * Returns the LDAP user search filter.
     *
     * @return the search filter
     */
    public String getUserSearchFilter() {
        return userSearchFilter;
    }

    /**
     * Sets the LDAP user search filter.
     *
     * @param userSearchFilter the search filter
     */
    public void setUserSearchFilter(final String userSearchFilter) {
        this.userSearchFilter = userSearchFilter;
    }

    /**
     * Returns the bind DN for LDAP connections.
     *
     * @return the bind DN
     */
    public String getBindDn() {
        return bindDn;
    }

    /**
     * Sets the bind DN for LDAP connections.
     *
     * @param bindDn the bind DN
     */
    public void setBindDn(final String bindDn) {
        this.bindDn = bindDn;
    }

    /**
     * Returns the bind password for LDAP connections.
     *
     * @return the bind password
     */
    public String getBindPassword() {
        return bindPassword;
    }

    /**
     * Sets the bind password for LDAP connections.
     *
     * @param bindPassword the bind password
     */
    public void setBindPassword(final String bindPassword) {
        this.bindPassword = bindPassword;
    }
}
