package com.bugboard.api.auth;

import com.bugboard.api.models.User;
import com.bugboard.api.repositories.UserRepositoryAdaptee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("dev")
public class DevAuthService implements AuthService {
    private final UserRepositoryAdaptee userRepositoryAdaptee;

    public DevAuthService(UserRepositoryAdaptee userRepositoryAdaptee) {
        this.userRepositoryAdaptee = userRepositoryAdaptee;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return userRepositoryAdaptee.findByEmail("test@bugboard.local");
    }
}
