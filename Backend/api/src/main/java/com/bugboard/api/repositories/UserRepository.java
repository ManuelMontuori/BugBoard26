package com.bugboard.api.repositories;

import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // soliti metodi CRUD sono già disponibili grazie a JpaRepository

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUuidAndStatus(UUID uuid, UserStatus status);

//    void deleteByUuid(String uuid);

    boolean existsByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(UserStatus status);
}
