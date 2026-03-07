package com.erp.kernel.businesslogic.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Contextual data passed to business rules during evaluation.
 *
 * <p>A rule context carries named attributes that rules can read to make
 * decisions. The context is immutable once created via the builder.
 */
public final class RuleContext {

    private final String entityType;
    private final String operation;
    private final Map<String, Object> attributes;

    private RuleContext(final String entityType,
                        final String operation,
                        final Map<String, Object> attributes) {
        this.entityType = entityType;
        this.operation = operation;
        this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Returns the entity type this context applies to.
     *
     * @return the entity type name
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Returns the operation being performed (e.g., CREATE, UPDATE, DELETE).
     *
     * @return the operation name
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Returns all context attributes as an unmodifiable map.
     *
     * @return the attributes map
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Returns the value of a named attribute.
     *
     * @param name the attribute name
     * @return an {@link Optional} containing the value, or empty if not present
     */
    public Optional<Object> getAttribute(final String name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(attributes.get(name));
    }

    /**
     * Creates a new builder for constructing a rule context.
     *
     * @param entityType the entity type name
     * @param operation  the operation name
     * @return a new builder
     */
    public static Builder builder(final String entityType, final String operation) {
        return new Builder(entityType, operation);
    }

    /**
     * Builder for creating {@link RuleContext} instances.
     */
    public static final class Builder {

        private final String entityType;
        private final String operation;
        private final Map<String, Object> attributes = new HashMap<>();

        private Builder(final String entityType, final String operation) {
            this.entityType = Objects.requireNonNull(entityType, "entityType must not be null");
            this.operation = Objects.requireNonNull(operation, "operation must not be null");
        }

        /**
         * Adds an attribute to the context.
         *
         * @param name  the attribute name
         * @param value the attribute value
         * @return this builder
         */
        public Builder attribute(final String name, final Object value) {
            Objects.requireNonNull(name, "name must not be null");
            attributes.put(name, value);
            return this;
        }

        /**
         * Builds the rule context.
         *
         * @return the constructed context
         */
        public RuleContext build() {
            return new RuleContext(entityType, operation, attributes);
        }
    }
}
