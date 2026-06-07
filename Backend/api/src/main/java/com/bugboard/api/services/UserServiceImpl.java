package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.dto.UserWorkloadOutDTO;

import com.bugboard.api.mapper.UserMapper;
import com.bugboard.api.mapper.UserReportMapper;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;

import com.bugboard.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.lang.System;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserReadService userReadService;
    private final UserWriteService userWriteService;
    public UserServiceImpl(UserReadService userReadService,
                           UserWriteService userWriteService) {
        this.userReadService = userReadService;
        this.userWriteService = userWriteService;
    }

    @Override
    public UserDTO create(UserDTO dto) {
        return userWriteService.create(dto);
    }

    @Override
    public List<UserDTO> findAll() {
        return userReadService.findAll();
    }

    @Override
    public void disableUser(UUID uuid) {
        userWriteService.disableUser(uuid);
    }

    @Override
    public void enableUser(UUID uuid) {
        userWriteService.enableUser(uuid);
    }

    @Override
    public List<UserDTO> findAllDisabledUsers() {
        return userReadService.findAllDisabledUsers();
    }

    @Override
    public List<UserWorkloadOutDTO> findByWorkload() {
        return userReadService.findByWorkload();
    }

    @Override
    public Optional<UserDTO> findByUuid(UUID uuid) {
        return userReadService.findByUuid(uuid);
    }

    @Override
    public List<UserReportDTO> getMonthlyReport(int year, int month) {
        return userReadService.getMonthlyReport(year, month);
    }
}

