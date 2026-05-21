package com.bugboard.api.repositories;

import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.dto.WorkloadDTO;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;
import com.bugboard.api.services.UserServiceTarget;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserServiceTarget {

    private final UserRepositoryAdaptee userRepositoryAdaptee;
    public UserRepositoryAdapter(UserRepositoryAdaptee userRepositoryAdaptee) {
        this.userRepositoryAdaptee = userRepositoryAdaptee;
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userRepositoryAdaptee.findByUuid(uuid);
    }

    @Override
    public Optional<User> findByUuidAndStatus(UUID uuid, UserStatus status) {
        return userRepositoryAdaptee.findByUuidAndStatus(uuid, status);
    }

    @Override
    public boolean existsByUuid(UUID uuid) {
        return userRepositoryAdaptee.existsByUuid(uuid);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryAdaptee.findByEmail(email);
    }

    @Override
    public List<User> findAllByStatus(UserStatus status) {
        return userRepositoryAdaptee.findAllByStatus(status);
    }

    @Override
    public List<User> findAll() {
        return userRepositoryAdaptee.findAll();
    }

    @Override
    public User save(User user) {
        return userRepositoryAdaptee.save(user);
    }

    @Override
    public List<WorkloadDTO> findByWorkload() {
        return userRepositoryAdaptee.findByWorkload();
    }

    @Override
    public List<UserReportDTO> getUserReports(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepositoryAdaptee.getUserReports(startDate, endDate);
    }

}
