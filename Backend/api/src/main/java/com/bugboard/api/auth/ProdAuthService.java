package com.bugboard.api.auth;

import com.bugboard.api.models.User;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Profile("prod")
public class ProdAuthService implements AuthService {

    private final UserRepository userRepository;

    public ProdAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return Optional.empty();
        }

        if (!(auth.getPrincipal() instanceof Jwt jwt)) {
            return Optional.empty();
        }


        String uuidStr = jwt.getClaimAsString("custom:uuid");
        if (uuidStr == null || uuidStr.isBlank()) {
            return Optional.empty();
        }

        UUID uuid = UUID.fromString(uuidStr);
        return userRepository.findByUuid(uuid);
    }
}