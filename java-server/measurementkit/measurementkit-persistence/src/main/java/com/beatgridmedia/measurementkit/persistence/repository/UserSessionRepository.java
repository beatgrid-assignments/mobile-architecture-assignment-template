package com.beatgridmedia.measurementkit.persistence.repository;

import com.beatgridmedia.measurementkit.persistence.entity.UserEntity;
import com.beatgridmedia.measurementkit.persistence.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {

    List<UserSessionEntity> findByUserAndRevokedAtIsNull(UserEntity user);
}
