package com.bugboard.api.mapper;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUuid().toString(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name(),
                user.getLastLogin(),
                user.getCreatedAt()
        );
    }

    public void mapToEntity(UserDTO dto, User user) {
        user.setEmail(dto.email());
        user.setRole(UserRole.valueOf(dto.role()));
    }
}
