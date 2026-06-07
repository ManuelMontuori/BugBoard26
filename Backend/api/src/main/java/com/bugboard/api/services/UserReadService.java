package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.dto.UserWorkloadOutDTO;
import com.bugboard.api.mapper.UserMapper;
import com.bugboard.api.mapper.UserReportMapper;
import com.bugboard.api.models.UserStatus;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserReadService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserReportMapper userReportMapper;
    public UserReadService(UserRepository userRepository,
                           UserMapper userMapper,
                           UserReportMapper userReportMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userReportMapper = userReportMapper;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    public List<UserDTO> findAllDisabledUsers() {
        return userRepository.findAllByStatus(UserStatus.DISABLED).stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    public List<UserWorkloadOutDTO> findByWorkload() {
        return userRepository.findByWorkload()
                .stream()
                .map(userMapper::mapWorkloadToWorkloadOut)
                .toList();
    }

    public Optional<UserDTO> findByUuid(UUID uuid) {
//        UUID userUuid = UUID.fromString(uuid); // converto la stringa in UUID
        return userRepository.findByUuidAndStatus(uuid, UserStatus.ACTIVE).map(userMapper::mapToDTO);
    }

    public List<UserReportDTO> getMonthlyReport(int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);
        System.out.println("inizio: "+ startDate);
        System.out.println("fine: "+ endDate);
        return userRepository.getUserReports(startDate, endDate)
                .stream()
                .map(userReportMapper::mapToDTO)
                .toList();
    }


}
