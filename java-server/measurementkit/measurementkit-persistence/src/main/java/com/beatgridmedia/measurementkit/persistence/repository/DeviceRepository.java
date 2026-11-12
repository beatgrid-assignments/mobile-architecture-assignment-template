package com.beatgridmedia.measurementkit.persistence.repository;

import com.beatgridmedia.measurementkit.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    Optional<DeviceEntity> findByInstallationId(String installationId);
}
