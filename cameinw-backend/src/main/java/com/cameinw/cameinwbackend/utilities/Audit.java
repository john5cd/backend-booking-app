package com.cameinw.cameinwbackend.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Audit class is a base class for entities that require auditing of creation and modification timestamps.
 * It includes fields for tracking the creation timestamp (`createdAt`) and the last modification timestamp (`updatedAt`).
 * This class is intended to be used as a superclass for entities that need auditing functionality.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
public class Audit implements Serializable {

    /**
     * The timestamp indicating when the entity was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * The timestamp indicating when the entity was last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;


    /**
     * Gets the creation timestamp of the entity.
     *
     * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the entity.
     *
     * @param createdAt The creation timestamp to set.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last modification timestamp of the entity.
     *
     * @return The last modification timestamp.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    /**
     * Sets the last modification timestamp of the entity.
     *
     * @param updatedAt The last modification timestamp to set.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
