package com.bugboard.api.mapper;

import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.dto.UserWorkloadOutDTO;
import com.bugboard.api.dto.WorkloadDTO;
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
        user.setLastName(dto.lastName());
        user.setFirstName(dto.firstName());
    }

    public UserWorkloadOutDTO mapWorkloadToWorkloadOut(WorkloadDTO dto) {
        return new UserWorkloadOutDTO(
                dto.user().getUuid().toString(),
                dto.user().getFirstName(),
                dto.user().getLastName(),
                dto.issuesCount()
        );
    }
}
