package com.bugboard.api.repositories;

import com.bugboard.api.dto.WorkloadDTO;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryAdaptee extends JpaRepository<User, Long> {
    // soliti metodi CRUD sono già disponibili grazie a JpaRepository

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUuidAndStatus(UUID uuid, UserStatus status);

//    void deleteByUuid(String uuid);

    boolean existsByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(UserStatus status);

    @Query("""
        SELECT new com.bugboard.api.dto.WorkloadDTO(u, COUNT(i))
        FROM User u
        LEFT JOIN Issue i ON i.assignedTo = u 
          AND i.status != com.bugboard.api.models.IssueStatus.DONE
        WHERE u.status = com.bugboard.api.models.UserStatus.ACTIVE
        GROUP BY u.id, u.uuid, u.email, u.role, u.status, u.lastLogin, u.createdAt
        ORDER BY COUNT(i) ASC
    """)
    List<WorkloadDTO> findByWorkload();
}
