package com.erp.kernel.sysvar;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation of {@link SystemVariableProvider}.
 *
 * <p>Resolves system variables using the current JVM runtime state.
 * Date and time values are evaluated on each call to reflect the current instant.
 * User, language, and client values default to configurable or fixed values.
 */
@Service
public class DefaultSystemVariableProvider implements SystemVariableProvider {

    private static final DateTimeFormatter DATS_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    private static final String DEFAULT_CLIENT = "000";
    private static final String DEFAULT_LANGUAGE = "E";
    private static final String SYSTEM_ID = "ERP";

    private final String hostName;

    /**
     * Creates the default system variable provider, resolving the host name at startup.
     */
    public DefaultSystemVariableProvider() {
        this(resolveHostName());
    }

    /**
     * Creates a system variable provider with the specified host name.
     *
     * @param hostName the host name to use
     */
    DefaultSystemVariableProvider(final String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String getValue(final SystemVariable variable) {
        Objects.requireNonNull(variable, "variable must not be null");
        return switch (variable) {
            case SY_DATUM -> LocalDate.now().format(DATS_FORMATTER);
            case SY_UZEIT -> LocalTime.now().format(TIMS_FORMATTER);
            case SY_UNAME -> resolveUserName();
            case SY_LANGU -> DEFAULT_LANGUAGE;
            case SY_MANDT -> DEFAULT_CLIENT;
            case SY_HOST -> hostName;
            case SY_SYSID -> SYSTEM_ID;
            case SY_PAGNO -> "0";
            case SY_SUBRC -> "0";
            case SY_INDEX -> "0";
            case SY_DBCNT -> "0";
        };
    }

    @Override
    public Map<SystemVariable, String> getAllValues() {
        final var values = new EnumMap<SystemVariable, String>(SystemVariable.class);
        for (final var variable : SystemVariable.values()) {
            values.put(variable, getValue(variable));
        }
        return Collections.unmodifiableMap(values);
    }

    private String resolveUserName() {
        final var userName = System.getProperty("user.name");
        return userName != null ? userName : "SYSTEM";
    }

    static String resolveHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (final java.net.UnknownHostException e) {
            return "UNKNOWN";
        }
    }
}
