package com.erp.kernel.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry for managing extension points and their contributions.
 *
 * <p>Plugins register contributions against named extension points.
 * The kernel queries the registry to retrieve all contributions for a
 * given extension point. Thread-safe for concurrent access.
 */
@Component
public class ExtensionRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionRegistry.class);

    private final ConcurrentMap<String, List<Object>> extensions = new ConcurrentHashMap<>();

    /**
     * Registers a contribution for the given extension point.
     *
     * @param extensionPoint the extension point
     * @param contribution   the contribution to register
     * @param <T>            the contribution type
     */
    public <T> void register(final ExtensionPoint<T> extensionPoint,
                             final T contribution) {
        Objects.requireNonNull(extensionPoint, "extensionPoint must not be null");
        Objects.requireNonNull(contribution, "contribution must not be null");
        extensions.computeIfAbsent(extensionPoint.getName(),
                k -> new CopyOnWriteArrayList<>()).add(contribution);
        LOG.info("Registered extension for point: {}", extensionPoint.getName());
    }

    /**
     * Returns all contributions for the given extension point.
     *
     * @param extensionPoint the extension point
     * @param <T>            the contribution type
     * @return an unmodifiable list of contributions
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getExtensions(final ExtensionPoint<T> extensionPoint) {
        Objects.requireNonNull(extensionPoint, "extensionPoint must not be null");
        final var list = extensions.getOrDefault(
                extensionPoint.getName(), List.of());
        return Collections.unmodifiableList((List<T>) list);
    }

    /**
     * Checks whether any contributions exist for the given extension point.
     *
     * @param extensionPoint the extension point
     * @return {@code true} if at least one contribution is registered
     */
    public boolean hasExtensions(final ExtensionPoint<?> extensionPoint) {
        Objects.requireNonNull(extensionPoint, "extensionPoint must not be null");
        final var list = extensions.getOrDefault(extensionPoint.getName(), List.of());
        return !list.isEmpty();
    }

    /**
     * Removes all registered extensions. Primarily for testing.
     */
    public void clear() {
        extensions.clear();
    }
}
