package com.erp.kernel.ddic.repository;

import com.erp.kernel.ddic.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Domain} entities.
 */
public interface DomainRepository extends JpaRepository<Domain, Long> {

    /**
     * Finds a domain by its unique name.
     *
     * @param domainName the domain name to search for
     * @return an {@link Optional} containing the domain, or empty if not found
     */
    Optional<Domain> findByDomainName(String domainName);

    /**
     * Checks whether a domain with the given name exists.
     *
     * @param domainName the domain name to check
     * @return {@code true} if a domain with this name exists
     */
    boolean existsByDomainName(String domainName);
}
