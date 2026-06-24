package org.frontend.models.dtos;

public record UserDTO(
        String uuid,
        String email,
        String role
) {}