package com.erp.kernel.sysvar;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for accessing read-only system variables.
 *
 * <p>Provides endpoints at {@code /api/v1/system/variables} for retrieving
 * all system variables or a specific variable by name.
 */
@RestController
@RequestMapping("/api/v1/system/variables")
public class SystemVariableController {

    private final SystemVariableProvider systemVariableProvider;

    /**
     * Creates a new system variable controller.
     *
     * @param systemVariableProvider the system variable provider
     */
    public SystemVariableController(final SystemVariableProvider systemVariableProvider) {
        this.systemVariableProvider = Objects.requireNonNull(
                systemVariableProvider, "systemVariableProvider must not be null");
    }

    /**
     * Returns all system variables with their current values.
     *
     * @return a list of system variable DTOs
     */
    @GetMapping
    public ResponseEntity<List<SystemVariableDto>> getAll() {
        final var allValues = systemVariableProvider.getAllValues();
        final var dtos = allValues.entrySet().stream()
                .map(entry -> new SystemVariableDto(
                        entry.getKey().getVariableName(),
                        entry.getValue(),
                        entry.getKey().getDescription(),
                        entry.getKey().getDataType()))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Returns a single system variable by its enum name.
     *
     * @param name the system variable enum name (e.g., SY_DATUM)
     * @return the system variable DTO, or 404 if not found
     */
    @GetMapping("/{name}")
    public ResponseEntity<SystemVariableDto> getByName(@PathVariable final String name) {
        try {
            final var variable = SystemVariable.valueOf(name);
            final var value = systemVariableProvider.getValue(variable);
            return ResponseEntity.ok(new SystemVariableDto(
                    variable.getVariableName(),
                    value,
                    variable.getDescription(),
                    variable.getDataType()));
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
