package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.AuthorizationObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link AuthorizationObject} entity persistence operations.
 */
public interface AuthorizationObjectRepository extends JpaRepository<AuthorizationObject, Long> {

    /**
     * Finds an authorisation object by name.
     *
     * @param objectName the object name
     * @return the authorisation object if found
     */
    Optional<AuthorizationObject> findByObjectName(String objectName);

    /**
     * Checks whether an authorisation object exists with the given name.
     *
     * @param objectName the object name
     * @return {@code true} if an object with the name exists
     */
    boolean existsByObjectName(String objectName);
}
