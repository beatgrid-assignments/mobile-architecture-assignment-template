package com.beatgridmedia.measurementkit.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.beatkit.persistence.entity.AbstractEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

/**
 * A user (panel member), identified by a verified phone number.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends AbstractEntity {

    /**
     * Phone number in E.164 format (e.g. +31612345678).
     */
    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    /**
     * ISO 3166-1 alpha-2 country code, derived from the phone number.
     */
    @Column(length = 2)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 16)
    private UserStatus status = UserStatus.ACTIVE;

    private Instant lastLoginAt;

    public UserEntity(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
