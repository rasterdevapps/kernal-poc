package com.erp.kernel.security.service;

import com.erp.kernel.security.auth.LocalAuthenticationProvider;
import com.erp.kernel.security.dto.CreateUserRequest;
import com.erp.kernel.security.dto.UserDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.mapper.UserMapper;
import com.erp.kernel.security.repository.UserRepository;
import com.erp.kernel.security.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for user management.
 *
 * <p>Manages user CRUD operations, role assignments, and account status
 * similar to SAP SU01 user administration.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * Creates a new user service.
     *
     * @param userRepository     the user repository
     * @param userRoleRepository the user-role repository
     */
    public UserService(final UserRepository userRepository,
                       final UserRoleRepository userRoleRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.userRoleRepository = Objects.requireNonNull(userRoleRepository, "userRoleRepository must not be null");
    }

    /**
     * Creates a new user.
     *
     * @param request the creation request
     * @return the created user DTO
     * @throws DuplicateEntityException if a user with the same username or email exists
     */
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto create(final CreateUserRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateEntityException(
                    "User with username '%s' already exists".formatted(request.username()));
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEntityException(
                    "User with email '%s' already exists".formatted(request.email()));
        }
        final var entity = UserMapper.toEntity(request);
        if (request.password() != null && !request.password().isBlank()) {
            entity.setPasswordHash(LocalAuthenticationProvider.hashPassword(request.password()));
            entity.setPasswordChangedAt(Instant.now());
        }
        final var saved = userRepository.save(entity);
        LOG.info("User '{}' created with ID {}", saved.getUsername(), saved.getId());
        return UserMapper.toDto(saved);
    }

    /**
     * Finds a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     * @throws EntityNotFoundException if the user is not found
     */
    @Cacheable(value = "users", key = "#id")
    public UserDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id %d not found".formatted(id)));
        return UserMapper.toDto(entity);
    }

    /**
     * Returns all users.
     *
     * @return the list of all user DTOs
     */
    @Cacheable(value = "users", key = "'all'")
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing user.
     *
     * @param id      the user ID
     * @param request the update request
     * @return the updated user DTO
     * @throws EntityNotFoundException  if the user is not found
     * @throws DuplicateEntityException if the new username or email conflicts
     */
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto update(final Long id, final CreateUserRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id %d not found".formatted(id)));
        userRepository.findByUsername(request.username())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "User with username '%s' already exists".formatted(request.username()));
                });
        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "User with email '%s' already exists".formatted(request.email()));
                });
        UserMapper.updateEntity(entity, request);
        final var saved = userRepository.save(entity);
        LOG.info("User '{}' updated", saved.getUsername());
        return UserMapper.toDto(saved);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @throws EntityNotFoundException if the user is not found
     */
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "User with id %d not found".formatted(id));
        }
        userRoleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        LOG.info("User with ID {} deleted", id);
    }
}
