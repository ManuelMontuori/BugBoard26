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

    // la dependency injection sostituisce l'Autowired. Il been container gentisce automaticamentel'inejction
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserReportMapper userReportMapper;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserReportMapper userReportMapper) {
        this.userRepository = userRepository;
        this.userMapper=userMapper;
        this.userReportMapper = userReportMapper;
    }


    @Override
    public UserDTO create(UserDTO dto) {
        User user = new User();
        userMapper.mapToEntity(dto, user);
        User saved = userRepository.save(user);
        return userMapper.mapToDTO(saved);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    @Override
    public void disableUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
        user.disable();
    }

    @Override
    public void enableUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
        user.enable();
    }

    @Override
    public List<UserDTO> findAllDisabledUsers() {
        return userRepository.findAllByStatus(UserStatus.DISABLED).stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    @Override
    public List<UserWorkloadOutDTO> findByWorkload() {
        return userRepository.findByWorkload()
                .stream()
                .map(userMapper::mapWorkloadToWorkloadOut)
                .toList();
    }

    @Override
    public Optional<UserDTO> findByUuid(UUID uuid) {
//        UUID userUuid = UUID.fromString(uuid); // converto la stringa in UUID
        return userRepository.findByUuidAndStatus(uuid, UserStatus.ACTIVE).map(userMapper::mapToDTO);
    }

   

    @Override
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

