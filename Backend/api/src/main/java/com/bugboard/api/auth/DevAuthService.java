package com.bugboard.api.auth;

import com.bugboard.api.models.User;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("dev")
public class DevAuthService implements AuthService {
    private final UserRepository userRepository;

    public DevAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return userRepository.findByEmail("test@bugboard.local");
    }
}
