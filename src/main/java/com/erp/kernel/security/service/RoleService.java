package com.erp.kernel.security.service;

import com.erp.kernel.security.dto.CreateRoleRequest;
import com.erp.kernel.security.dto.RoleDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.mapper.RoleMapper;
import com.erp.kernel.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for role management.
 *
 * <p>Manages role CRUD operations for role-based access control (RBAC),
 * similar to SAP PFCG role maintenance.
 */
@Service
@Transactional(readOnly = true)
public class RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    /**
     * Creates a new role service.
     *
     * @param roleRepository the role repository
     */
    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = Objects.requireNonNull(roleRepository, "roleRepository must not be null");
    }

    /**
     * Creates a new role.
     *
     * @param request the creation request
     * @return the created role DTO
     * @throws DuplicateEntityException if a role with the same name exists
     */
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public RoleDto create(final CreateRoleRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new DuplicateEntityException(
                    "Role with name '%s' already exists".formatted(request.roleName()));
        }
        final var entity = RoleMapper.toEntity(request);
        final var saved = roleRepository.save(entity);
        LOG.info("Role '{}' created with ID {}", saved.getRoleName(), saved.getId());
        return RoleMapper.toDto(saved);
    }

    /**
     * Finds a role by ID.
     *
     * @param id the role ID
     * @return the role DTO
     * @throws EntityNotFoundException if the role is not found
     */
    @Cacheable(value = "roles", key = "#id")
    public RoleDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role with id %d not found".formatted(id)));
        return RoleMapper.toDto(entity);
    }

    /**
     * Returns all roles.
     *
     * @return the list of all role DTOs
     */
    @Cacheable(value = "roles", key = "'all'")
    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing role.
     *
     * @param id      the role ID
     * @param request the update request
     * @return the updated role DTO
     * @throws EntityNotFoundException  if the role is not found
     * @throws DuplicateEntityException if the new name conflicts with another role
     */
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public RoleDto update(final Long id, final CreateRoleRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role with id %d not found".formatted(id)));
        roleRepository.findByRoleName(request.roleName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Role with name '%s' already exists".formatted(request.roleName()));
                });
        RoleMapper.updateEntity(entity, request);
        final var saved = roleRepository.save(entity);
        LOG.info("Role '{}' updated", saved.getRoleName());
        return RoleMapper.toDto(saved);
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the role ID
     * @throws EntityNotFoundException if the role is not found
     */
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Role with id %d not found".formatted(id));
        }
        roleRepository.deleteById(id);
        LOG.info("Role with ID {} deleted", id);
    }
}
