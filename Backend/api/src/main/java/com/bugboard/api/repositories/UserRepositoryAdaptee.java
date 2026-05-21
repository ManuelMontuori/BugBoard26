package com.bugboard.api.repositories;

import com.bugboard.api.dto.WorkloadDTO;
import com.bugboard.api.models.User;
import com.bugboard.api.models.UserStatus;
import com.bugboard.api.repositories.projection.UserReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query(value = """
SELECT 
    u.uuid AS uuid,
    u.firstname AS firstName,
    u.lastname AS lastName,
    u.email AS email,

    COUNT(i.id) AS totIssues,

    SUM(CASE 
        WHEN i.reporter = u.id THEN 1 
        ELSE 0 
    END) AS totCreatedIssues,

    SUM(CASE 
        WHEN i.assigned_to = u.id AND i.is_status = 'DONE' THEN 1 
        ELSE 0 
    END) AS totResolvedIssues,

    SUM(CASE 
        WHEN i.assigned_to = u.id THEN 1 
        ELSE 0 
    END) AS totWorkloadIssues,

    ROUND(AVG(
    CASE
    WHEN i.resolved_at IS NOT NULL
    THEN EXTRACT(EPOCH FROM (i.resolved_at - i.created_at)) / 3600
    END), 2) AS averageIssues,

    SUM(CASE 
        WHEN i.assigned_to = u.id 
             AND i.is_priority = 'HIGH' 
             AND i.is_status = 'DONE'
        THEN 1 
        ELSE 0 
    END) AS totHighPriorityIssues

FROM users u
LEFT JOIN issues i 
    ON (i.assigned_to = u.id OR i.reporter = u.id)
    AND i.created_at >= :startDate
    AND i.created_at < :endDate

GROUP BY u.id, u.uuid, u.firstname, u.lastname, u.email

""", nativeQuery = true)
    List<UserReportProjection> getUserReports(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
