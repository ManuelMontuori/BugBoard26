package com.bugboard.api.services;

import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.dto.WorkloadDTO;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;
import com.bugboard.api.repositories.projection.UserReportProjection;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserServiceTarget {
    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUuidAndStatus(UUID uuid, UserStatus status);

    boolean existsByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(UserStatus status);

    List<User> findAll();

    User save(User user);

    List<WorkloadDTO> findByWorkload();

    List<UserReportProjection> getUserReports(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
