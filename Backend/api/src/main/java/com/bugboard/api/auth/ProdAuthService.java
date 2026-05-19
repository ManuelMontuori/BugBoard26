package com.bugboard.api.auth;

import com.bugboard.api.models.User;
import com.bugboard.api.repositories.UserRepositoryAdaptee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("prod")
public class ProdAuthService implements AuthService {

    private final UserRepositoryAdaptee userRepositoryAdaptee;

    public ProdAuthService(UserRepositoryAdaptee userRepositoryAdaptee) {
        this.userRepositoryAdaptee = userRepositoryAdaptee;
    }

    @Override
    public Optional<User> getCurrentUser() {
        // Da utilizzare quando si metterà su aws, per ora ritorna null,
        // in quanto non è implementato il sistema di autenticazione
        return null;
    }
}
