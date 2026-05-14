package com.bugboard.api.auth;

import com.bugboard.api.models.User;

import java.util.Optional;

public interface AuthService {
    Optional<User> getCurrentUser();
}
