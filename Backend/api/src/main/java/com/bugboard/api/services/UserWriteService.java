package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.mapper.UserMapper;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserWriteService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserWriteService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO create(UserDTO dto) {
        User user = new User();
        userMapper.mapToEntity(dto, user);
        User saved = userRepository.save(user);
        return userMapper.mapToDTO(saved);
    }

    public void disableUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
        user.disable();
    }

    public void enableUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
        user.enable();
    }



}
