package com.bugboard.api.services;

import com.bugboard.api.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDTO create(UserDTO dto);

    List<UserDTO> findAll();

    Optional<UserDTO> findByUuid(UUID uuid);

    UserDTO update(String uuid, UserDTO dto);

    void delete(String uuid);

    void disableUser(UUID uuid);

    void enableUser(UUID uuid);

    List<UserDTO> findAllDisabledUsers();

    List<UserDTO> findByWorkload();

}
