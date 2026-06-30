package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.mapper.UserMapper;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

@Service
public class UserWriteService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CognitoUserService cognitoUserService;
    public UserWriteService(UserRepository userRepository, UserMapper userMapper, CognitoUserService cognitoUserService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cognitoUserService = cognitoUserService;
    }

    public UserDTO create(UserDTO dto) {
        User user = new User();
        userMapper.mapToEntity(dto, user);
        User saved = userRepository.save(user);
        cognitoUserService.registraUtenteSuCognito(saved);
        return userMapper.mapToDTO(saved);
    }

    public void disableUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new ResourceAccessException("User not found"));
        user.disable();
    }

    public void enableUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new ResourceAccessException("User not found"));
        user.enable();
    }



}
