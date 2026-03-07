package com.erp.kernel.ddic.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BaseEntity}.
 */
class BaseEntityTest {

    /**
     * Concrete subclass for testing the abstract BaseEntity.
     */
    private static class TestEntity extends BaseEntity {
    }

    @Test
    void shouldSetTimestampsOnCreate() {
        final var entity = new TestEntity();
        entity.onCreate();

        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getCreatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void shouldUpdateTimestampOnUpdate() {
        final var entity = new TestEntity();
        entity.onCreate();
        final var originalUpdatedAt = entity.getUpdatedAt();

        entity.onUpdate();

        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
    }

    @Test
    void shouldGetAndSetId() {
        final var entity = new TestEntity();
        entity.setId(42L);

        assertThat(entity.getId()).isEqualTo(42L);
    }

    @Test
    void shouldReturnNullIdWhenNotPersisted() {
        final var entity = new TestEntity();

        assertThat(entity.getId()).isNull();
    }

    @Test
    void shouldGetAndSetCreatedAt() {
        final var entity = new TestEntity();
        final var now = Instant.now();
        entity.setCreatedAt(now);

        assertThat(entity.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldGetAndSetUpdatedAt() {
        final var entity = new TestEntity();
        final var now = Instant.now();
        entity.setUpdatedAt(now);

        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldBeEqualWhenSameId() {
        final var entity1 = new TestEntity();
        entity1.setId(1L);
        final var entity2 = new TestEntity();
        entity2.setId(1L);

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        final var entity1 = new TestEntity();
        entity1.setId(1L);
        final var entity2 = new TestEntity();
        entity2.setId(2L);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        final var entity = new TestEntity();
        entity.setId(1L);

        assertThat(entity).isNotEqualTo(null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        final var entity = new TestEntity();
        entity.setId(1L);

        assertThat(entity).isNotEqualTo("not an entity");
    }

    @Test
    void shouldBeEqualToSelf() {
        final var entity = new TestEntity();
        entity.setId(1L);

        assertThat(entity).isEqualTo(entity);
    }

    @Test
    void shouldHaveSameHashCodeWhenSameId() {
        final var entity1 = new TestEntity();
        entity1.setId(1L);
        final var entity2 = new TestEntity();
        entity2.setId(1L);

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenDifferentId() {
        final var entity1 = new TestEntity();
        entity1.setId(1L);
        final var entity2 = new TestEntity();
        entity2.setId(2L);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    void shouldHandleNullIdInEquals() {
        final var entity1 = new TestEntity();
        final var entity2 = new TestEntity();

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    void shouldHandleNullIdInHashCode() {
        final var entity = new TestEntity();

        assertThat(entity.hashCode()).isEqualTo(0);
    }
}
