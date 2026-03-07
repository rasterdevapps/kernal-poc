package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents an SAP-style authorisation object that groups related access checks.
 *
 * <p>Authorisation objects define the fields and values that are checked during
 * access control, similar to SAP's authorisation concept with object classes.
 */
@Entity
@Table(name = "auth_authorization_object")
public class AuthorizationObject extends BaseEntity {

    @Column(name = "object_name", nullable = false, unique = true, length = 100)
    private String objectName;

    @Column(name = "object_class", nullable = false, length = 100)
    private String objectClass;

    @Column(name = "description", length = 500)
    private String description;

    /**
     * Returns the authorisation object name.
     *
     * @return the object name
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Sets the authorisation object name.
     *
     * @param objectName the object name
     */
    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    /**
     * Returns the object class.
     *
     * @return the object class
     */
    public String getObjectClass() {
        return objectClass;
    }

    /**
     * Sets the object class.
     *
     * @param objectClass the object class
     */
    public void setObjectClass(final String objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
